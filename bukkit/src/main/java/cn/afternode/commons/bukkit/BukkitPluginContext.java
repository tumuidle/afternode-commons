package cn.afternode.commons.bukkit;

import cn.afternode.commons.ReflectionError;
import cn.afternode.commons.bukkit.annotations.RegisterCommand;
import cn.afternode.commons.bukkit.annotations.RegisterListener;
import cn.afternode.commons.bukkit.annotations.RegisterPluginCommand;
import cn.afternode.commons.bukkit.message.MessageBuilder;
import cn.afternode.commons.bukkit.message.TabBuilder;
import cn.afternode.commons.localizations.ILocalizations;
import cn.afternode.commons.serialization.FieldAccessException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class BukkitPluginContext {
    private final Plugin plugin;

    private ComponentLike messageLinePrefix = Component.text().build();
    private ILocalizations localizations = null;

    /**
     * @param plg Plugin instance will be used in registrations
     */
    public BukkitPluginContext(Plugin plg) {
        this.plugin = plg;
    }

    /**
     * Find classes with provided package name and register as CommandExecutor/TabExecutor
     * @param packageName Target package name
     * @throws RuntimeException Not a valid CommandExecutor/TabExecutor; Unable to create instance
     * @see RegisterPluginCommand
     */
    public void registerPluginCommands(String packageName) throws RuntimeException {
        Reflections ref = new Reflections(packageName);
        Set<Class<?>> classes =
                ref.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(RegisterPluginCommand.class)).asClass());

        for (Class<?> c: classes) {
            try {
                if (
                        !CommandExecutor.class.isAssignableFrom(c)
                                && !TabCompleter.class.isAssignableFrom(c)
                                && !TabExecutor.class.isAssignableFrom(c)
                ) throw new IllegalArgumentException("%s is not a valid CommandExecutor/TabExecutor but a RegisterPluginCommand annotation was present".formatted(c.getName()));

                RegisterPluginCommand anno = c.getAnnotation(RegisterPluginCommand.class);
                PluginCommand cmd = Bukkit.getPluginCommand(anno.name());
                if (cmd == null) throw new NullPointerException("Cannot find plugin command %s".formatted(anno.name()));

                Constructor<?> constructor = c.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();

                if (instance instanceof CommandExecutor) cmd.setExecutor((CommandExecutor) instance);
                if (instance instanceof TabCompleter) cmd.setTabCompleter((TabCompleter) instance);
            } catch (Throwable t) {
                throw new RuntimeException("Cannot register %s as a plugin command".formatted(c.getName()), t);
            }
        }
    }

    /**
     * Find classes with provided package name and register as Command, target class must extend org.bukkit.command.Command
     * <br>
     * This method will register commands through CommandMap
     * @param packageName Package name for scanning
     * @see CommandMap
     * @see Command
     * @see RegisterCommand
     * @throws ReflectionError Error in reflections
     */
    public void registerCommands(String packageName) {
        Reflections ref = new Reflections(packageName);
        Set<Class<?>> classes =
                ref.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(RegisterCommand.class)).asClass());

        SimpleCommandMap map = BukkitReflections.getCommandMap();

        for (Class<?> c: classes) {
            if (!Command.class.isAssignableFrom(c)) throw new IllegalArgumentException("%s is not a valid Command class but a RegisterCommand annotation was present".formatted(c.getName()));

            Constructor<Command> cons;
            try {
                cons = (Constructor<Command>) c.getDeclaredConstructor();
            } catch (NoSuchMethodException e){
                throw new ReflectionError(c, e);
            }
            cons.trySetAccessible();
            Command inst;
            try {
                inst = cons.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ReflectionError(cons, c, e);
            }

            map.register(plugin.getName(), inst);
        }
    }

    /**
     * Find classes with provided package name and register as event listener
     * @param packageName Target package name
     * @see RegisterListener
     */
    public void registerListeners(String packageName) {
        Reflections ref = new Reflections(packageName);
        Set<Class<?>> classes =
                ref.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(RegisterPluginCommand.class)).asClass());

        for (Class<?> c: classes) {
            try {
                if (!Listener.class.isAssignableFrom(c))
                    throw new IllegalArgumentException("%s is not assignable from org.bukkit.event.Listener".formatted(c.getName()));

                Constructor<? extends Listener> constructor = (Constructor<? extends Listener>) c.getDeclaredConstructor();
                constructor.setAccessible(true);

                Bukkit.getPluginManager().registerEvents(constructor.newInstance(), plugin);
            } catch (Throwable t) {
                throw new RuntimeException("Cannot register %s as an event listener".formatted(c.getName()), t);
            }
        }
    }

    /**
     * Set plugin display name
     * @param name Target name
     * @see PluginDescriptionFile#getName()
     */
    public void setDisplayName(String name) {
        Field field = null;
        try {
            field = PluginDescriptionFile.class.getDeclaredField("name");
            field.trySetAccessible();
            field.set(plugin.getDescription(), name);
        } catch (NoSuchFieldException e) {
            throw new ReflectionError(PluginDescriptionFile.class, e);
        } catch (IllegalAccessException e) {
            throw new FieldAccessException(field, e);
        }
    }

    /**
     * Create MessageBuilder with localizations and prefix in this context
     * @param sender Sender passed to MessageBuilder
     * @return builder
     */
    public MessageBuilder message(CommandSender sender) {
        return new MessageBuilder().localizations(localizations).linePrefix(messageLinePrefix).sender(sender);
    }

    /**
     * Create MessageBuilder with localizations and prefix in this context
     * @return builder
     */
    public MessageBuilder message() {
        return new MessageBuilder().localizations(localizations).linePrefix(messageLinePrefix);
    }

    /**
     * Get plugin of this context
     * @return Plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get message line prefix of this context
     * @return Message line prefix
     */
    public ComponentLike getMessageLinePrefix() {
        return messageLinePrefix;
    }

    /**
     * Set message line prefix of this context
     * @param messageLinePrefix Prefix
     */
    public void setMessageLinePrefix(ComponentLike messageLinePrefix) {
        this.messageLinePrefix = messageLinePrefix;
    }

    /**
     * Get localizations of this context
     * @return Localizations
     */
    public ILocalizations getLocalizations() {
        return localizations;
    }

    /**
     * Set localizations of this context
     * @param localizations Localizations
     */
    public void setLocalizations(ILocalizations localizations) {
        this.localizations = localizations;
    }
}
