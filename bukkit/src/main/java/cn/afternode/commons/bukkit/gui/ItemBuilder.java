package cn.afternode.commons.bukkit.gui;

import cn.afternode.commons.bukkit.message.MessageBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Tool for create an item
 */
public class ItemBuilder {
    private Material material;
    private Component name = null;
    private final List<Component> lore = new ArrayList<>();
    private Integer model = -1;
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final Set<ItemFlag> flags = new HashSet<>();
    private boolean unbreakable = false;
    private final Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();

    /**
     * Primary constructor
     * @param material {@link ItemBuilder#material}
     */
    public ItemBuilder(Material material) {
        this.material(material);
    }

    /**
     * Set material for this item
     * @param material material
     * @return this
     */
    public ItemBuilder material(Material material) {
        if (material.isAir())
            throw new IllegalArgumentException("Cannot create air item");

        this.material = material;
        return this;
    }

    /**
     * Get material of this item
     * @return material
     */
    public Material material() {
        return this.material;
    }

    /**
     * Set name of this item
     * @param name Name or null as default
     * @return this
     * @see org.bukkit.inventory.meta.ItemMeta#displayName(Component)
     */
    public ItemBuilder name(ComponentLike name) {
        this.name = name.asComponent();
        return this;
    }

    /**
     * Set name of this item
     * @param name Name or null as default
     * @return this
     */
    public ItemBuilder name(Component name) {
        this.name = name;
        return this;
    }

    /**
     * Set name of this item from builder
     * @param builder builder
     * @return this
     */
    public ItemBuilder name(MessageBuilder builder) {
        return this.name(builder.build());
    }

    /**
     * Get name of this item
     * @return name
     */
    public Component name() {
        return this.name;
    }

    /**
     * Add lore to this item
     * @param lore lore
     * @return this
     */
    public ItemBuilder lore(ComponentLike... lore) {
        for (ComponentLike c : lore) {
            this.lore.add(c.asComponent());
        }
        return this;
    }

    /**
     * Add lore to this item
     * @param lore lore
     * @return this
     */
    public ItemBuilder lore(MessageBuilder... lore) {
        for (MessageBuilder c : lore) {
            this.lore.add(c.build());
        }
        return this;
    }

    /**
     * Add lore to this item
     * @param lore lore
     * @return this
     */
    public ItemBuilder lore(Component... lore) {
        Collections.addAll(this.lore, lore);
        return this;
    }

    /**
     * Reset lore of this item
     * @return this
     */
    public ItemBuilder newLore() {
        this.lore.clear();
        return this;
    }

    /**
     * Set <a href="https://minecraft.wiki/w/Tutorials/Models#Item_predicates">custom model data</a> for this item, null to clear
     * @param model model
     * @return this
     * @see ItemMeta#setCustomModelData(Integer)
     */
    public ItemBuilder model(Integer model) {
        this.model = model;
        return this;
    }

    /**
     * Get current custom model data
     * @return data
     */
    public Integer model() {
        return this.model;
    }

    /**
     * Set enchantment level for this item
     * @param enchantment enchantment
     * @param level level
     * @return this
     * @see ItemMeta#addEnchant(Enchantment, int, boolean)
     */
    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    /**
     * Remove enchantment from this item
     * @param enchantment enchantment
     * @return this
     */
    public ItemBuilder removeEnchant(Enchantment enchantment) {
        this.enchantments.remove(enchantment);
        return this;
    }

    /**
     * Get enchantment level of this item
     * @param enchantment enchantment
     * @return level
     */
    public Integer enchant(Enchantment enchantment) {
        return this.enchantments.getOrDefault(enchantment, null);
    }

    /**
     * Clear existing enchantments
     * @return this
     */
    public ItemBuilder clearEnchantments() {
        this.enchantments.clear();
        return this;
    }

    /**
     * Add flags for this item
     * @param flags flags
     * @return this
     */
    public ItemBuilder flag(ItemFlag... flags) {
        Collections.addAll(this.flags, flags);
        return this;
    }

    /**
     * Remove flags from this item
     * @param flags flags
     * @return this
     */
    public ItemBuilder removeFlags(ItemFlag... flags) {
        for (ItemFlag flag : flags) {
            this.flags.remove(flag);
        }
        return this;
    }

    /**
     * Get flags from this item
     * @return flags
     */
    public Set<ItemFlag> flags() {
        return new HashSet<>(this.flags);
    }

    /**
     * Clear existing flags
     * @return this
     */
    public ItemBuilder clearFlags() {
        this.flags.clear();
        return this;
    }

    /**
     * Set unbreakable tag
     * @param unbreakable value
     * @return this
     * @see ItemMeta#setUnbreakable(boolean)
     */
    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    /**
     * Get unbreakable tag value
     * @return value
     */
    public boolean unbreakable() {
        return this.unbreakable;
    }

    /**
     * Add attribute modifier
     * @param attribute attribute
     * @param modifiers modifiers
     * @return this
     * @see ItemMeta#addAttributeModifier(Attribute, AttributeModifier)
     */
    public ItemBuilder attribute(Attribute attribute, AttributeModifier... modifiers) {
        for (AttributeModifier modifier : modifiers) {
            this.attributes.put(attribute, modifier);
        }
        return this;
    }

    /**
     * Get modifiers of an attribute
     * @param attribute attribute
     * @return modifiers
     */
    public Collection<AttributeModifier> attribute(Attribute attribute) {
        return this.attributes.get(attribute);
    }

    /**
     * Get existing attribute modifiers from this item
     * @return modifiers
     */
    public Multimap<Attribute, AttributeModifier> attributes() {
        return HashMultimap.create(this.attributes);
    }

    /**
     * Clear existing attribute modifiers
     * @return this
     */
    public ItemBuilder clearAttributes() {
        this.attributes.clear();
        return this;
    }

    /**
     * Convert to item
     * @return result item
     */
    public ItemStack build() {
        ItemStack stack = new ItemStack(this.material);

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(this.material);
        if (this.name != null)
            meta.displayName(this.name);    // name
        if (!this.lore.isEmpty())
            meta.lore(this.lore);   // lore
        meta.setCustomModelData(this.model);    // model
        this.enchantments.forEach((e, l) -> meta.addEnchant(e, l, true));   // enchantment
        meta.addItemFlags(this.flags.toArray(new ItemFlag[0])); // flags
        meta.setUnbreakable(this.unbreakable);  // unbreakable
        this.attributes.forEach(meta::addAttributeModifier);    // attributes
        stack.setItemMeta(meta);    // Apply to stack

        return stack;
    }
}
