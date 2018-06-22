package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.common.annotation.ServiceLog;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.common.util.DateUtil;
import com.sf.arch.udata.privilege.dao.PrivilegeDAO;
import com.sf.arch.udata.privilege.dao.RolePrivilegeDAO;
import com.sf.arch.udata.privilege.pojo.PrivilegeDO;
import com.sf.arch.udata.privilege.pojo.ResponseData;
import com.sf.arch.udata.privilege.pojo.RolePrivilegeDO;
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
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeDAO dao;

    @Autowired
    private RolePrivilegeDAO rpDao;

    @Autowired
    EntityManagerFactory factory;

    @Override
    @ServiceLog("创建权限")
    public ResponseData savePrivilege(PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        if(dao.findByPrivilegeNameAndProductName(privilegeDO.getPrivilegeName(), privilegeDO.getProductName()).size() != 0)
            return AppUtil.responseData(ErrorMsg.PRIVILEGE_UNIQUE_ERROR);
        return AppUtil.responseData(dao.save(privilegeDO));
    }

    @Override
    @ServiceLog("更新权限状态")
    public ResponseData updateStatus(Long id, Integer status){
        if(null == id || null == status)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        return AppUtil.responseData("修改成功，影响行数：" + dao.updateStatus(id, status, DateUtil.getDateInt()));
    }

    @Override
    @Transactional
    @ServiceLog("删除权限")
    public ResponseData removePrivilege(Long id){
        if(null == id)
            return AppUtil.responseData(ErrorMsg.ID_NULL_ERROR);
        rpDao.removeRolePrivilegeDOByPrivilegeId(id);
        dao.delete(id);
        return AppUtil.responseData("删除成功, id=" + id);
    }

    @Override
    public ResponseData findPrivilege(PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        if(StringUtils.isEmpty(privilegeDO.getProductName()) || null == privilegeDO.getPrivilegeType())
            return AppUtil.responseData(ErrorMsg.CONDITION_NULL_ERROR);
        return AppUtil.responseData(dao.findByProductNameAndPrivilegeType(privilegeDO.getProductName(), privilegeDO.getPrivilegeType()));
    }

    @Override
    @ServiceLog("修改权限")
    public ResponseData updatePrivilege(PrivilegeDO privilegeDO){
        if(privilegeDO == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        if(privilegeDO.getId() == null)
            return AppUtil.responseData(ErrorMsg.ID_NULL_ERROR);
        int line = dao.updatePrivilegeDOById(privilegeDO.getId(),
                privilegeDO.getPrivilegeName(),
                privilegeDO.getDescription(),
                privilegeDO.getProductName(),
                privilegeDO.getPrivilegeType(),
                privilegeDO.getPrivilegeAction(),
                privilegeDO.getUtime());
        return AppUtil.responseData("修改成功，影响行数：" + line);
    }

    @Override
    public Long getMaxId(){
        EntityManager em = factory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PrivilegeDO> root = cq.from(PrivilegeDO.class);
        cq.select(cb.greatest((Path)root.get("id")));
        TypedQuery<Long> typedQuery = em.createQuery(cq);
        return (null == typedQuery) ? null : typedQuery.getSingleResult();
    }

    @Override
    public ResponseData getPage(int page, int size){
        Pageable pageable = new PageRequest(page, size);
        return AppUtil.responseData(dao.findAll(pageable));
    }

    @Override
    public ResponseData findBy(PrivilegeDO obj, int page, int size){
        if(obj == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        Pageable pageable = new PageRequest(page, size);
        Page<PrivilegeDO> result = dao.findAll(new Specification<PrivilegeDO>() {
            @Override
            public Predicate toPredicate(Root<PrivilegeDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list1 = new ArrayList<>();
                if(!StringUtils.isEmpty(obj.getPrivilegeName())){
                    list1.add(cb.like(root.get("privilegeName").as(String.class), "%" + obj.getPrivilegeName() + "%"));
                }
                if(null != obj.getStatus()){
                    list1.add(cb.equal(root.get("status").as(Integer.class), obj.getStatus()));
                }
                if(!StringUtils.isEmpty(obj.getProductName())){
                    list1.add(cb.like(root.get("productName").as(String.class), "%" + obj.getProductName() + "%"));
                }
                Predicate[] p = new Predicate[list1.size()];
                return cb.and(list1.toArray(p));
            }
        }, pageable);
        return AppUtil.responseData(result);
    }

    @Override
    public ResponseData findBy(PrivilegeDO obj){
        if(obj == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        //if status equals -1, then return all items.
        if(Integer.valueOf(-1).equals(obj.getStatus())){
            obj.setStatus(null);
        }
        List<PrivilegeDO> result = dao.findAll(new Specification<PrivilegeDO>() {
            @Override
            public Predicate toPredicate(Root<PrivilegeDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list1 = new ArrayList<>();
                if(!StringUtils.isEmpty(obj.getPrivilegeName())){
                    list1.add(cb.like(root.get("privilegeName").as(String.class), "%" + obj.getPrivilegeName() + "%"));
                }
                if(null != obj.getStatus()){
                    list1.add(cb.equal(root.get("status").as(Integer.class), obj.getStatus()));
                }
                if(!StringUtils.isEmpty(obj.getProductName())){
                    list1.add(cb.like(root.get("productName").as(String.class), "%" + obj.getProductName() + "%"));
                }
                Predicate[] p = new Predicate[list1.size()];
                return cb.and(list1.toArray(p));
            }
        });
        return AppUtil.responseData(result);
    }

    public List<RolePrivilegeDO> findRolePrivilegeDOByRoleIds(Long[] roleIds){
        List<RolePrivilegeDO> list = new ArrayList<>();
        if(roleIds != null) {
            for (Long id : roleIds) {
                list.addAll(rpDao.findAllByRoleId(id));
            }
        }
        return list;
    }

    //date : 20180329
    @Override
    public ResponseData findPrivilegeByRoleIds(Long[] roleIds){
        List<RolePrivilegeDO> list = findRolePrivilegeDOByRoleIds(roleIds);
        Iterator<RolePrivilegeDO> iter = list.iterator();
        Set<Long> ids = new HashSet<>();
        while (iter.hasNext()){
            RolePrivilegeDO item = iter.next();
            //if status is zero, then ignore
            PrivilegeDO privilegeDO = dao.findOne(item.getPrivilegeId());
            if(privilegeDO !=null && privilegeDO.getStatus() == 1)
                ids.add(item.getPrivilegeId());
        }
        return AppUtil.responseData(dao.findAll(ids));
    }

    @Override
    public ResponseData findAll(){
        //return privileges is open.
        return AppUtil.responseData(dao.findAllByStatusOrderByCtimeDesc(1));
    }

    @Override
    public int findStatusById(Long id) {
        PrivilegeDO privilegeDO = dao.findOne(id);
        if(privilegeDO != null)
            return privilegeDO.getStatus();
        return 0;
    }

    @Override
    public List<PrivilegeDO> findByUid(String uid){
        return dao.findByUid(uid);
    }
}
