package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.common.Constant;
import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.common.util.DateUtil;
import com.sf.arch.udata.privilege.common.util.PrivilegeUtil;
import com.sf.arch.udata.privilege.dao.*;
import com.sf.arch.udata.privilege.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;


@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDAO dao;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private PrivilegeDAO pDao;

    @Autowired
    private AccountRoleDAO arDao;

    @Autowired
    private RoleDAO rDao;

    @Autowired
    private RolePrivilegeDAO rpDao;

    @Autowired
    private EntityManagerFactory factory;

    @Override
    @Transactional
    public ResponseData removeRole(Long id) {
        if(null == id)
            return AppUtil.responseData(ErrorMsg.ID_NULL_ERROR);
        rpDao.removeRolePrivilegeDOByRoleId(id);
        dao.delete(id);
        return AppUtil.responseData("删除成功, id=" + id);
    }

    @Override
    public ResponseData saveRole(RoleDO role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        if(dao.findByRoleName(role.getRoleName()).size() != 0)
            return AppUtil.responseData(ErrorMsg.ROLE_UNIQUE_ERROR);
        return  AppUtil.responseData(dao.save(role));
    }

    @Override
    @Transactional
    public ResponseData saveRole(RoleDO role, Long[] privilegeIds){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        if(dao.findByRoleName(role.getRoleName()).size() != 0)
            return AppUtil.responseData(ErrorMsg.ROLE_UNIQUE_ERROR);
        role.setCtime(DateUtil.getDateInt());
        role.setUtime(DateUtil.getDateInt());
        role.setStatus(1);
        role.setRoleNO(AppUtil.generateNO("role", getMaxId()));
        RoleDO roleDO = dao.save(role);
        bindPrivilege(roleDO.getId(), privilegeIds);
        return AppUtil.responseData(roleDO);
    }

    @Override
    public ResponseData updateStatus(Long id, Integer status){
        if(null == id || null == status){
            return AppUtil.responseData(ErrorMsg.STATUS_NULL_ERROR);
        }
        return AppUtil.responseData("修改成功，影响行数：" + dao.updateStatus(id, status, DateUtil.getDateInt()));
    }

    @Override
    public  ResponseData updateRole(RoleDO role){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        if(role.getId() == null)
            return AppUtil.responseData(ErrorMsg.ID_NULL_ERROR);
        int line = dao.updateRoleDOById(role.getId(), role.getRoleName(), role.getDescription(), role.getUtime());
        return AppUtil.responseData("修改成功，影响行数：" + line);
    }

    @Override
    @Transactional
    public  ResponseData updateRole(RoleDO role, Long[] privilegeIds){
        if(role == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        rpDao.removeRolePrivilegeDOByRoleId(role.getId());
        rpDao.flush();
        int line = dao.updateRoleDOById(role.getId(), role.getRoleName(), role.getDescription(), role.getUtime());
        bindPrivilege(role.getId(), privilegeIds);
        return AppUtil.responseData("修改成功，影响行数：" + line);
    }

    @Override
    public Long getMaxId(){
        EntityManager em = factory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<RoleDO> root = cq.from(RoleDO.class);
        cq.select(cb.greatest((Path)root.get("id")));
        TypedQuery<Long> typedQuery = em.createQuery(cq);
        return (null == typedQuery) ? null : typedQuery.getSingleResult();
    }

    @Override
    public ResponseData bindPrivilege(Long roleId, Long[] privilegeIds){
        List<RolePrivilegeDO> lst = new ArrayList<>();
        for (Long id: privilegeIds) {
            RolePrivilegeDO obj = new RolePrivilegeDO();
            obj.setDescription("");
            obj.setRoleId(roleId);
            obj.setPrivilegeId(id);
            obj.setStatus(1);       //open
            obj.setCtime(DateUtil.getDateInt());
            obj.setUtime(DateUtil.getDateInt());
            lst.add(obj);
        }
        Iterable<RolePrivilegeDO> iterable = lst;
        return AppUtil.responseData(rpDao.save(iterable));
    }

    @Override
    public ResponseData getPage(int page, int size){
        Pageable pageable = new PageRequest(page, size);
        return AppUtil.responseData(dao.findAll(pageable));
    }

    @Override
    public ResponseData findBy(RoleDO obj, int page, int size){
        if(obj == null)
            return AppUtil.responseData(ErrorMsg.CONDITION_NULL_ERROR);
        Pageable pageable = new PageRequest(page, size);
        Page<RoleDO> result = dao.findAll(new Specification<RoleDO>() {
            @Override
            public Predicate toPredicate(Root<RoleDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list1 = new ArrayList<>();
                if(!StringUtils.isEmpty(obj.getRoleName())){
                    list1.add(cb.like(root.get("roleName").as(String.class), "%" + obj.getRoleName() + "%"));
                }
                if(null != obj.getStatus()){
                    list1.add(cb.equal(root.get("status").as(Integer.class), obj.getStatus()));
                }
                Predicate[] p = new Predicate[list1.size()];
                return cb.and(list1.toArray(p));
            }
        }, pageable);
        return AppUtil.responseData(result);
    }

    @Override
    public ResponseData findBy(RoleDO obj){
        if(obj == null)
            return AppUtil.responseData(ErrorMsg.CONDITION_NULL_ERROR);
        //if status equal -1, then return all items.
        if(Integer.valueOf(-1).equals(obj.getStatus())) {
            obj.setStatus(null);
        }
        List<RoleJson> result = new ArrayList<>();
        List<RoleDO> list = dao.findAll(new Specification<RoleDO>() {
            @Override
            public Predicate toPredicate(Root<RoleDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list1 = new ArrayList<>();
                if(!StringUtils.isEmpty(obj.getRoleName())){
                    list1.add(cb.like(root.get("roleName").as(String.class), "%" + obj.getRoleName() + "%"));
                }
                if(null != obj.getStatus()){
                    list1.add(cb.equal(root.get("status").as(Integer.class), obj.getStatus()));
                }
                Predicate[] p = new Predicate[list1.size()];
                return cb.and(list1.toArray(p));
            }
        });

        List<Long> roleIds = new ArrayList<>();
        Map<Long, RoleDO> rMap = new HashMap<>();
        for(RoleDO roleDO : list) {
            roleIds.add(roleDO.getId());
            rMap.put(roleDO.getId(), roleDO);
        }

        List<PrivilegeDO> pList = pDao.findByRoleIds(roleIds);
        Map<Long, PrivilegeDO> pMap = new HashMap<>();
        for (PrivilegeDO privilegeDO : pList){
            pMap.put(privilegeDO.getId(), privilegeDO);
        }


        List<RolePrivilegeDO> rpList = rpDao.findAllByRoleIds(roleIds);
        Map<Long, List<PrivilegeDO>> rpMap = new HashMap<>();
        for(RolePrivilegeDO rp : rpList){
            if(rpMap.containsKey(rp.getRoleId())){
                rpMap.get(rp.getRoleId()).add(pMap.get(rp.getPrivilegeId()));
            }
            else{
                List<PrivilegeDO> tmp = new ArrayList<>();
                tmp.add(pMap.get(rp.getPrivilegeId()));
                rpMap.put(rp.getRoleId(), tmp);
            }
        }

        Iterator<Map.Entry<Long,List<PrivilegeDO>>> iterator = rpMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Long, List<PrivilegeDO>> entry = iterator.next();
            Long id = entry.getKey();
            List<PrivilegeDO> tmpList = entry.getValue();
            Map<String,List<PrivilegeDO>> map = new HashMap<>();
            for(PrivilegeDO p : tmpList){
                if(map.containsKey(p.getProductName()))
                    map.get(p.getProductName()).add(p);
                else{
                    List<PrivilegeDO> tmp = new ArrayList<>();
                    tmp.add(p);
                    map.put(p.getProductName(), tmp);
                }
            }
            RoleJson json = new RoleJson();
            json.setRole(rMap.get(id));
            List<RoleJson.Privileges> psList = new ArrayList<>();
            Iterator<Map.Entry<String,List<PrivilegeDO>>> tmpIterator = map.entrySet().iterator();
            while (tmpIterator.hasNext()){
                Map.Entry<String,List<PrivilegeDO>> tmpEntry = tmpIterator.next();
                RoleJson.Privileges privileges = json.new Privileges();
                privileges.setProductName(tmpEntry.getKey());
                privileges.setPrivileges(tmpEntry.getValue());
                psList.add(privileges);
            }
            json.setPrivileges(psList);
            result.add(json);
        }

        return AppUtil.responseData(result);
    }

    @Override
    public int findStatusById(Long id) {
        RoleDO roleDO = dao.findOne(id);
        if(roleDO != null){
            return roleDO.getStatus();
        }
        return 0;
    }

    @Override
    public ResponseData findAll(){
        List<RoleDO> list = dao.findAllByStatus(Constant.STATUS_OPEN);
        return AppUtil.responseData(list);
    }

    @Override
    public List<PrivilegeDO> findPrivileges(String uid) {
        List<PrivilegeDO> result = new ArrayList<>();
        List<AccountDO> list = accountDAO.findByUidAndStatus(uid, Constant.STATUS_OPEN);
        if (list !=null && list.size() > 0) {
            Long id = list.get(0).getId();
            List<AccountRoleDO> list1 = arDao.findAllByAccountId(id);
            Set<Long> ids = new HashSet<>();
            for (AccountRoleDO item1 : list1) {
                Long roleId = item1.getRoleId();
                RoleDO roleDO = rDao.findOne(roleId);
                if (roleDO != null && roleDO.getStatus() != 0) {
                    List<RolePrivilegeDO> list2 = rpDao.findAllByRoleId(roleId);
                    for (RolePrivilegeDO item2 : list2) {
                        PrivilegeDO privilegeDO = pDao.findOne(item2.getPrivilegeId());
                        if(privilegeDO != null && privilegeDO.getStatus() == 1)
                            result.add(privilegeDO);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ResponseData findAllByUid(String uid){
        if(accountDAO.findByUidAndStatus(uid, Constant.STATUS_OPEN).size() == 0)
            return AppUtil.responseData(new ArrayList<RoleDO>());

        List<PrivilegeDO> pList = pDao.findByUid(uid);
        List<Long> pidList = new ArrayList<>();
        for(PrivilegeDO p : pList){
            pidList.add(p.getId());
        }

        List<RolePrivilegeDO> rpList = rpDao.findOpenItems();
        Map<Long, List<Long>> map = new HashMap<>();
        for(RolePrivilegeDO rp : rpList){
            if(map.containsKey(rp.getRoleId()))
                map.get(rp.getRoleId()).add(rp.getPrivilegeId());
            else{
                List<Long> tmp = new ArrayList<>();
                tmp.add(rp.getPrivilegeId());
                map.put(rp.getRoleId(), tmp);
            }
        }

        List<RoleDO> list = dao.findAllByStatus(Constant.STATUS_OPEN);
        Iterator<RoleDO> iterator = list.iterator();
        while (iterator.hasNext()){
            RoleDO roleDO = iterator.next();
            if(map.containsKey(roleDO.getId())){
                List<Long> ids = map.get(roleDO.getId());
                if(!PrivilegeUtil.lessThanIds(ids, pidList))
                    iterator.remove();
            }else
                iterator.remove();
        }

        return AppUtil.responseData(list);
    }
}