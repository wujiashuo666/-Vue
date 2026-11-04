package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.HrMapper;
import org.javaboy.vhr.mapper.HrRoleMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HrService implements UserDetailsService {
    @Autowired
    HrMapper hrMapper;
    @Autowired
    HrRoleMapper hrRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username);
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        hr.setRoles(hrMapper.getHrRolesById(hr.getId()));
        return hr;
    }

    public List<Hr> getAllHrs(String keywords) {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().getId(),keywords);
    }

    public Integer updateHr(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    @Transactional
    public boolean updateHrRole(Integer hrid, Integer[] rids) {
        hrRoleMapper.deleteByHrid(hrid);
        //清空角色是合法操作：rids 为空时不再执行 addRole，
        //否则 MyBatis foreach 对 null 求值会抛 BuilderException
        if (rids == null || rids.length == 0) {
            return true;
        }
        return hrRoleMapper.addRole(hrid, rids) == rids.length;
    }

    /**
     * 新增操作员：校验登录名唯一并加密初始密码，可选一并分配角色
     */
    @Transactional
    public RespBean addHr(Hr hr) {
        if (hr.getName() == null || hr.getName().trim().isEmpty()) {
            return RespBean.error("请填写姓名");
        }
        if (hr.getUsername() == null || hr.getUsername().trim().isEmpty()) {
            return RespBean.error("请填写登录用户名");
        }
        if (hrMapper.loadUserByUsername(hr.getUsername().trim()) != null) {
            return RespBean.error("登录用户名已存在");
        }
        if (hr.getPassword() == null || hr.getPassword().trim().isEmpty()) {
            return RespBean.error("请填写初始密码");
        }
        hr.setEnabled(true);
        //初始密码落库前 BCrypt 加密，与登录流程保持一致
        hr.setPassword(new BCryptPasswordEncoder().encode(hr.getPassword()));
        if (hrMapper.insertSelective(hr) != 1) {
            return RespBean.error("添加失败");
        }
        Integer[] roleIds = hr.getRoleIds();
        if (roleIds != null && roleIds.length > 0) {
            hrRoleMapper.deleteByHrid(hr.getId());
            hrRoleMapper.addRole(hr.getId(), roleIds);
        }
        return RespBean.ok("添加成功", hr);
    }

    public Integer deleteHrById(Integer id) {
        return hrMapper.deleteByPrimaryKey(id);
    }

    /**
     * 按 id 查询 hr，返回数据库中的最新资料
     */
    public Hr getHrById(Integer id) {
        return hrMapper.selectByPrimaryKey(id);
    }

    public Integer updateHyById(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    public boolean updateHrPasswd(String oldpass, String pass, Integer hrid) {
        Hr hr = hrMapper.selectByPrimaryKey(hrid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldpass, hr.getPassword())) {
            String encodePass = encoder.encode(pass);
            Integer result = hrMapper.updatePasswd(hrid, encodePass);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    public Integer updateUserface(String url, Integer id) {
        return hrMapper.updateUserface(url, id);
    }
}
