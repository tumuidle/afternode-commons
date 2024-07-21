module cn.afternode.commons.afternode.commons.bukkit.main {
    requires afternode.commons.commons.main;
    requires org.bukkit;
    requires org.reflections;
    requires org.jetbrains.annotations;

    requires net.kyori.adventure;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.examination.api;
    requires java.desktop;

    exports cn.afternode.commons.bukkit;
    exports cn.afternode.commons.bukkit.annotations;
    exports cn.afternode.commons.bukkit.configurations;
    exports cn.afternode.commons.bukkit.message;
}