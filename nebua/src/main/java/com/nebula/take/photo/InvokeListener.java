package com.nebula.take.photo;


import com.nebula.model.entity.InvokeParam;
import com.nebula.take.tips.PermissionManager;

/**
 * 授权管理回调
 */
public interface InvokeListener {
    PermissionManager.TPermissionType invoke(InvokeParam invokeParam);
}
