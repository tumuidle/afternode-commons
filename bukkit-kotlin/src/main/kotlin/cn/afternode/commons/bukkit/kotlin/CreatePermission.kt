package cn.afternode.commons.bukkit.kotlin

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import kotlin.reflect.KProperty

class CreatePermission(val node: String, val parent: Permission? = null, val default: PermissionDefault = PermissionDefault.OP, val onCreated: ((Permission) -> Unit)? = null) {
    private val created by lazy { create() }

    operator fun getValue(thisRef: CreatePermission, property: KProperty<*>
    ): Permission {
        return thisRef.created
    }

    private fun create(): Permission {
        val realName = StringBuilder(parent?.name ?: "")
        if (realName.isNotEmpty()) realName.append(".")
        realName.append(node)
        val perm = Permission(realName.toString())
        Bukkit.getPluginManager().addPermission(perm)
        onCreated?.invoke(perm)

        return perm
    }

    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Permission {
        return this.created
    }
}

/**
 * Create a permission
 * <br>
 * ex. val permission by createPermission("plugin.permission")
 * <br>
 * @see CreatePermission
 */
fun createPermission(node: String, parent: Permission? = null, default: PermissionDefault = PermissionDefault.OP, onCreated: ((Permission) -> Unit)? = null) =
    CreatePermission(node, parent, default, onCreated)
