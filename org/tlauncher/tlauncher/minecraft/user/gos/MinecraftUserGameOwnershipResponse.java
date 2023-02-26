package org.tlauncher.tlauncher.minecraft.user.gos;

import ch.qos.logback.core.joran.action.Action;
import java.util.List;
import java.util.Objects;
import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/gos/MinecraftUserGameOwnershipResponse.class */
public class MinecraftUserGameOwnershipResponse implements Validatable {
    private List<Item> items;

    public MinecraftUserGameOwnershipResponse(List<Item> items) {
        this.items = items;
    }

    public MinecraftUserGameOwnershipResponse() {
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinecraftUserGameOwnershipResponse that = (MinecraftUserGameOwnershipResponse) o;
        return Objects.equals(this.items, that.items);
    }

    public int hashCode() {
        if (this.items != null) {
            return this.items.hashCode();
        }
        return 0;
    }

    public List<Item> getItems() {
        return this.items;
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.preq.Validatable
    public void validate() {
        Validatable.notNull(this.items, "items");
        for (Item item : this.items) {
            item.validate();
        }
    }

    public String toString() {
        return "MinecraftUserGameOwnershipResponse{items=" + this.items + '}';
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/gos/MinecraftUserGameOwnershipResponse$Item.class */
    public static class Item implements Validatable {
        private String name;
        private String signature;

        public String getName() {
            return this.name;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Item item = (Item) o;
            if (!this.name.equals(item.name)) {
                return false;
            }
            return this.signature.equals(item.signature);
        }

        public int hashCode() {
            int result = this.name.hashCode();
            return (31 * result) + this.signature.hashCode();
        }

        public String toString() {
            return "Item{name='" + this.name + "', signature='" + this.signature + "'}";
        }

        @Override // org.tlauncher.tlauncher.minecraft.user.preq.Validatable
        public void validate() {
            Validatable.notEmpty(this.name, Action.NAME_ATTRIBUTE);
            Validatable.notEmpty(this.signature, "signature");
        }
    }
}
