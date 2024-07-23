package cn.afternode.commons.bukkit.kotlin

import cn.afternode.commons.bukkit.report.PluginInfoElement
import cn.afternode.commons.bukkit.report.PluginListElement
import cn.afternode.commons.bukkit.report.PluginReport
import org.bukkit.plugin.Plugin

/**
 * Create plugin report
 * @see PluginReport
 */
fun pluginReport(plugin: Plugin, block: PluginReport.() -> Unit): PluginReport {
    val report = PluginReport(plugin)
    block(report)
    return report
}

/**
 * @see PluginInfoElement
 * @see PluginReport.withPluginInfo
 */
fun PluginReport.info(plugin: Plugin, block: PluginInfoElement.() -> Unit) {
    val info = PluginInfoElement(plugin)
    block(info)
    this.appendElement(info)
}

/**
 * @see PluginListElement
 * @see PluginReport.withPluginList
 */
fun PluginReport.list(block: PluginListElement.() -> Unit) {
    val list = PluginListElement()
    block(list)
    this.appendElement(list)
}


