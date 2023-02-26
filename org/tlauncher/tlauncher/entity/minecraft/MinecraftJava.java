package org.tlauncher.tlauncher.entity.minecraft;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/MinecraftJava.class */
public class MinecraftJava {
    private Map<Long, CompleteMinecraftJava> jvm = Collections.synchronizedMap(new HashMap());

    public void setJvm(Map<Long, CompleteMinecraftJava> jvm) {
        this.jvm = jvm;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MinecraftJava) {
            MinecraftJava other = (MinecraftJava) o;
            if (other.canEqual(this)) {
                Object this$jvm = getJvm();
                Object other$jvm = other.getJvm();
                return this$jvm == null ? other$jvm == null : this$jvm.equals(other$jvm);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MinecraftJava;
    }

    public int hashCode() {
        Object $jvm = getJvm();
        int result = (1 * 59) + ($jvm == null ? 43 : $jvm.hashCode());
        return result;
    }

    public String toString() {
        return "MinecraftJava(jvm=" + getJvm() + ")";
    }

    public Map<Long, CompleteMinecraftJava> getJvm() {
        return this.jvm;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/MinecraftJava$CompleteMinecraftJava.class */
    public static class CompleteMinecraftJava {
        private Long id;
        private String name;
        private String path;
        private List<String> args;

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setArgs(List<String> args) {
            this.args = args;
        }

        public String toString() {
            return "MinecraftJava.CompleteMinecraftJava(id=" + getId() + ", name=" + getName() + ", path=" + getPath() + ", args=" + getArgs() + ")";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof CompleteMinecraftJava) {
                CompleteMinecraftJava other = (CompleteMinecraftJava) o;
                if (other.canEqual(this)) {
                    Object this$id = getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id != null) {
                            return false;
                        }
                    } else if (!this$id.equals(other$id)) {
                        return false;
                    }
                    Object this$name = getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name != null) {
                            return false;
                        }
                    } else if (!this$name.equals(other$name)) {
                        return false;
                    }
                    Object this$path = getPath();
                    Object other$path = other.getPath();
                    if (this$path == null) {
                        if (other$path != null) {
                            return false;
                        }
                    } else if (!this$path.equals(other$path)) {
                        return false;
                    }
                    Object this$args = getArgs();
                    Object other$args = other.getArgs();
                    return this$args == null ? other$args == null : this$args.equals(other$args);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof CompleteMinecraftJava;
        }

        public int hashCode() {
            Object $id = getId();
            int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
            Object $name = getName();
            int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
            Object $path = getPath();
            int result3 = (result2 * 59) + ($path == null ? 43 : $path.hashCode());
            Object $args = getArgs();
            return (result3 * 59) + ($args == null ? 43 : $args.hashCode());
        }

        public Long getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getPath() {
            return this.path;
        }

        public List<String> getArgs() {
            return this.args;
        }

        public static CompleteMinecraftJava create(Long id, String name, String path, List<String> args) {
            CompleteMinecraftJava m = new CompleteMinecraftJava();
            m.setId(id);
            m.setName(name);
            m.setPath(path);
            m.setArgs(args);
            return m;
        }
    }
}
