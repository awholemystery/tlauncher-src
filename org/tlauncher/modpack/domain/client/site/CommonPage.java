package org.tlauncher.modpack.domain.client.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/CommonPage.class */
public class CommonPage<T> {
    private Integer current;
    private Integer allPages;
    private boolean next;
    private List<T> content;
    private Long allElements;

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public void setAllPages(Integer allPages) {
        this.allPages = allPages;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setAllElements(Long allElements) {
        this.allElements = allElements;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CommonPage) {
            CommonPage<?> other = (CommonPage) o;
            if (other.canEqual(this) && isNext() == other.isNext()) {
                Object this$current = getCurrent();
                Object other$current = other.getCurrent();
                if (this$current == null) {
                    if (other$current != null) {
                        return false;
                    }
                } else if (!this$current.equals(other$current)) {
                    return false;
                }
                Object this$allPages = getAllPages();
                Object other$allPages = other.getAllPages();
                if (this$allPages == null) {
                    if (other$allPages != null) {
                        return false;
                    }
                } else if (!this$allPages.equals(other$allPages)) {
                    return false;
                }
                Object this$allElements = getAllElements();
                Object other$allElements = other.getAllElements();
                if (this$allElements == null) {
                    if (other$allElements != null) {
                        return false;
                    }
                } else if (!this$allElements.equals(other$allElements)) {
                    return false;
                }
                Object this$content = getContent();
                Object other$content = other.getContent();
                return this$content == null ? other$content == null : this$content.equals(other$content);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommonPage;
    }

    public int hashCode() {
        int result = (1 * 59) + (isNext() ? 79 : 97);
        Object $current = getCurrent();
        int result2 = (result * 59) + ($current == null ? 43 : $current.hashCode());
        Object $allPages = getAllPages();
        int result3 = (result2 * 59) + ($allPages == null ? 43 : $allPages.hashCode());
        Object $allElements = getAllElements();
        int result4 = (result3 * 59) + ($allElements == null ? 43 : $allElements.hashCode());
        Object $content = getContent();
        return (result4 * 59) + ($content == null ? 43 : $content.hashCode());
    }

    public String toString() {
        return "CommonPage(current=" + getCurrent() + ", allPages=" + getAllPages() + ", next=" + isNext() + ", content=" + getContent() + ", allElements=" + getAllElements() + ")";
    }

    public CommonPage(Integer current, Integer allPages, boolean next, List<T> content, Long allElements) {
        this.current = current;
        this.allPages = allPages;
        this.next = next;
        this.content = content;
        this.allElements = allElements;
    }

    public Integer getCurrent() {
        return this.current;
    }

    public Integer getAllPages() {
        return this.allPages;
    }

    public boolean isNext() {
        return this.next;
    }

    public List<T> getContent() {
        return this.content;
    }

    public Long getAllElements() {
        return this.allElements;
    }

    public <T> CommonPage() {
        this.current = 0;
        this.allPages = 0;
        this.next = false;
        this.content = new ArrayList();
        this.allElements = 0L;
    }

    public boolean isEmpty() {
        return Objects.isNull(this.content) || this.content.isEmpty();
    }
}
