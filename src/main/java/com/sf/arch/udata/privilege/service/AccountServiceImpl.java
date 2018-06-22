package com.sf.arch.udata.privilege.service;

import com.sf.arch.udata.privilege.common.ErrorMsg;
import com.sf.arch.udata.privilege.common.util.AppUtil;
import com.sf.arch.udata.privilege.common.util.DateUtil;
import com.sf.arch.udata.privilege.dao.*;
import com.sf.arch.udata.privilege.pojo.*;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Array;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDAO dao;

    @Autowired
    EntityManagerFactory factory;

    @Autowired
    private AccountRoleDAO arDao;

    @Autowired
    private RolePrivilegeDAO rpDao;

    @Autowired
    private PrivilegeDAO pDao;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public ResponseData saveAccount(AccountDO account){
        if(account == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        account.setCtime(DateUtil.getDateInt());
        account.setUtime(DateUtil.getDateInt());
        return AppUtil.responseData(dao.save(account));
    }

    @Override
    @Transactional
    public ResponseData saveAccount(AccountDO account, Long[] ids){
        if(account == null || ids == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        if(dao.findByUid(account.getUid()).size() != 0){
            return AppUtil.responseData(ErrorMsg.UID_UNIQUE_ERROR);
        }
        Long[] operatorIds = findPrivilegeIds(account.getOperatorUid());
        Long[] userIds = findPrivilegeIds(account.getUid());
        if(!checkLessThanOperator(userIds, operatorIds))
            return AppUtil.responseData(ErrorMsg.LESS_THAN_ERROR);
        account.setCtime(DateUtil.getDateInt());
        account.setUtime(DateUtil.getDateInt());
        AccountDO obj = dao.save(account);
        bindRole(obj.getId(), ids);
        return AppUtil.responseData(obj);
    }

    @Override
    public ResponseData removeAccount(Long id){
        if(id == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        dao.delete(id);
        return AppUtil.responseData();
    }

    @Override
    public ResponseData updateAccount(Long id, String uname, Integer status, String scope, String op_uid ){
        if(null == id){
            return AppUtil.responseData(ErrorMsg.ID_NULL_ERROR);
        }
        int utime = DateUtil.getDateInt();
        int line = dao.updateAccountDOById(id, uname, status, scope, op_uid, utime);
        return AppUtil.responseData("影响行数：" + line);
    }

    @Override
    @Transactional
    public ResponseData updateAccount(AccountDO account, Long[] roleIds){
        if(account == null || roleIds == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        //更新前检查
        Long[] operatorIds = findPrivilegeIds(account.getOperatorUid());
        Long[] userIds = findPrivilegeIds(account.getUid());
        if(!checkLessThanOperator(userIds, operatorIds))
            return AppUtil.responseData(ErrorMsg.LESS_THAN_ERROR);

        arDao.deleteAccountRoleDOByAccountId(account.getId());
        arDao.flush();
        account.setUtime(DateUtil.getDateInt());
        int line = dao.updateAccountDOById(account.getId(),account.getUname(),account.getStatus()
                ,account.getManageScope().toString(),account.getOperatorUid(),account.getUtime());
        bindRole(account.getId(), roleIds);
        return AppUtil.responseData("影响行数：" + line);
    }

    @Override
    public ResponseData findAll(){
        return AppUtil.responseData(dao.findAllByOrderByCtimeDesc());
    }

    @Override
    public ResponseData findAccountOrderBy(String col){
        if(col == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        Sort sort = new Sort(Sort.Direction.DESC, col);
        return AppUtil.responseData(dao.findAll(sort));
    }

    @Override
    public ResponseData getAccountById(Long id){
        return AppUtil.responseData(dao.findOne(id));
    }

    @Override
    public ResponseData pageAccountOrderBy(String orderBy, String order, int page, int size){
        Sort.Direction direction = Sort.Direction.ASC;
        if(null != order)
            if("desc".equals(order.toLowerCase()))
                direction = Sort.Direction.DESC;
        Sort sort = new Sort(direction, orderBy);
        Pageable pageable = new PageRequest(page, size, sort);
        return AppUtil.responseData(dao.findAll(pageable));
    }

    @Override
    public ResponseData getPage(int page, int size){
        Pageable pageable = new PageRequest(page, size);
        return AppUtil.responseData(dao.findAll(pageable));
    }

    @Override
    public ResponseData findBy(AccountDO obj, int page, int size){
        if(obj == null)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        Pageable pageable = new PageRequest(page, size);
        Page<AccountDO> result = dao.findAll(new Specification<AccountDO>() {
            @Override
            public Predicate toPredicate(Root<AccountDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list1 = new ArrayList<>();

                if(!StringUtils.isEmpty(obj.getUid())){
                    list1.add(cb.like(root.get("uid").as(String.class), "%" + obj.getUid() + "%"));
                }

                if(!StringUtils.isEmpty(obj.getUname())){
                    list1.add(cb.like(root.get("uname").as(String.class), "%" + obj.getUname() + "%"));
                }

                Predicate[] p = new Predicate[list1.size()];
                return cb.and(list1.toArray(p));
            }
        }, pageable);
        return AppUtil.responseData(result);
    }

    @Override
    public ResponseData findBy(AccountDO obj){
        if(null == obj)
            return AppUtil.responseData(ErrorMsg.NULL_POINTER_ERROR);
        //if status equals -1, then return all items.
        if(obj.getStatus()==null || obj.getStatus() == -1){
            obj.setStatus(null);
        }

        List<AccountJson> result = new ArrayList<>();
        List<AccountDO> list = dao.findAll(new Specification<AccountDO>() {
            @Override
            public Predicate toPredicate(Root<AccountDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list1 = new ArrayList<>();
                if(!StringUtils.isEmpty(obj.getUid())){
                    list1.add(cb.like(root.get("uid").as(String.class), "%" + obj.getUid() + "%"));
                }
                if(!StringUtils.isEmpty(obj.getUname())){
                    list1.add(cb.like(root.get("uname").as(String.class), "%" + obj.getUname() + "%"));
                }
                Predicate[] p = new Predicate[list1.size()];
                return cb.and(list1.toArray(p));
            }
        });

        List<Long> accountIds = new ArrayList<>();
        for(AccountDO accountDO : list)
            accountIds.add(accountDO.getId());

        List<RoleDO> roleList = roleDAO.findByAccountIds(accountIds);
        Map<Long, RoleDO> roleDOMap = new HashMap<>();
        for (RoleDO roleDO : roleList){
            roleDOMap.put(roleDO.getId(), roleDO);
        }

        Map<Long,List<Long>> map = new HashMap<>();
        List<AccountRoleDO> accountRoleDOS = arDao.findAllByAccountIds(accountIds);
        for(AccountRoleDO accountRoleDO : accountRoleDOS){
            if(map.containsKey(accountRoleDO.getAccountId()))
                map.get(accountRoleDO.getAccountId()).add(accountRoleDO.getRoleId());
            else{
                List tmp = new ArrayList<>();
                tmp.add(accountRoleDO.getRoleId());
                map.put(accountRoleDO.getAccountId(), tmp);
            }
        }

        for(AccountDO accountDO : list){
            AccountJson json = new AccountJson();
            json.setAccount(accountDO);
            List<Long> idList = map.get(accountDO.getId());
            List<RoleDO> roleDOS = new ArrayList<>();
            for(Long id : idList){
                roleDOS.add(roleDOMap.get(id));
            }
            json.setRoles(roleDOS);
            result.add(json);
        }

        return AppUtil.responseData(result);

        /*
        if(null != list){
            for(AccountDO accountDO : list){
                List<AccountRoleDO> list1= arDao.findAllByAccountId(accountDO.getId());
                List<RoleDO> list2 = new ArrayList<>();
                for (AccountRoleDO acountRole: list1) {
                    RoleDO roleDO = roleDAO.findOne(acountRole.getRoleId());
                    if(null != roleDO)
                        list2.add(roleDO);
                }
                AccountJson json = new AccountJson();
                json.setAccount(accountDO);
                json.setRoles(list2);
                result.add(json);
            }
        }*/

    }


    @Override
    public Long getMaxId(){
        EntityManager em = factory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AccountDO> root = cq.from(AccountDO.class);
        cq.select(cb.greatest((Path)root.get("id")));
        TypedQuery<Long> typedQuery = em.createQuery(cq);
        return (null == typedQuery) ? null : typedQuery.getSingleResult();
    }

    @Override
    public ResponseData bindRole(Long accountId, Long[] roleIds){
        List<AccountRoleDO> lst = new ArrayList<>();
        if(roleIds != null) {
            for (Long id : roleIds) {
                AccountRoleDO obj = new AccountRoleDO();
                obj.setDescription("");
                obj.setRoleId(id);
                obj.setAccountId(accountId);
                obj.setStatus(1);                       //open
                obj.setCtime(DateUtil.getDateInt());
                obj.setUtime(DateUtil.getDateInt());
                lst.add(obj);
            }
        }
        Iterable<AccountRoleDO> iterable = lst;
        return AppUtil.responseData(arDao.save(iterable));
    }

    @Override
    public List<PrivilegeDO> findPrivileges(String uid){
        List<PrivilegeDO> result = new ArrayList<>();
        AccountDO accountDO = findByUid(uid);

        //if account is null or account is closed
        if(accountDO == null || accountDO.getStatus() == 0)
            return result;

        Long id = accountDO.getId();
        List<AccountRoleDO> list1 = arDao.findAllByAccountId(id);
        Set<Long> ids = new HashSet<>();
        for(AccountRoleDO item1 : list1){
            Long roleId = item1.getRoleId();
            RoleDO roleDO = roleDAO.findOne(roleId);
            if(roleDO != null && roleDO.getStatus() > 0) {
                List<RolePrivilegeDO> list2 = rpDao.findAllByRoleId(roleId);
                for (RolePrivilegeDO item2 : list2) {
                    PrivilegeDO privilegeDO = pDao.findOne(item2.getPrivilegeId());
                    if(privilegeDO !=null && privilegeDO.getStatus() > 0)
                        ids.add(item2.getPrivilegeId());
                }
            }
        }
        result = pDao.findAll(ids);
        return result;
    }

    @Override
    public boolean checkLessThanOperator(Long[] ids, Long[] operatorIds) {
        List<Long> list1 = Arrays.asList(ids);
        List<Long> list2 = Arrays.asList(operatorIds);
        for (Long item1: list1) {
            if(!list2.contains(item1))
                return false;
        }
        return true;
    }

    @Override
    public Long[] findPrivilegeIds(String uid) {
        List<PrivilegeDO> list = findPrivileges(uid);
        Long[] ids = new Long[list.size()];
        for(int i = 0; i < ids.length; ++i){
            ids[i] = list.get(i).getId();
        }
        return ids;
    }

    @Override
    public AccountDO findByUid(String uid){
        List<AccountDO> list = dao.findByUid(uid);
        if(list == null || list.size()==0)
            return null;
        else
            return list.get(0);
    }

    @Override
    public int findStatusById(Long id) {
        AccountDO accountDO = dao.findOne(id);
        if(accountDO != null){
            return accountDO.getStatus();
        }
        return 0;
    }

    @Override
    public boolean isSuperAccount(AccountDO accountDO) {
        if(accountDO == null || accountDO.getStatus() == 0)
            return false;
        List<AccountRoleDO> list = arDao.findAllByAccountId(accountDO.getId());
        if(list != null && list.size()>0){
            for (AccountRoleDO item: list) {
                RoleDO roleDO = roleDAO.findOne(item.getRoleId());
                if(roleDO !=null && roleDO.getStatus()>0 && "超级管理员".equals(roleDO.getRoleName()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> findPrivileges(String uid, String uri){
        return pDao.findPrivilege(uid, uri);
    }
}
