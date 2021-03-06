/*
 * Copyright 2017-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.net.pi.runtime;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Action group of a protocol-independent pipeline.
 */
@Beta
public final class PiActionGroup {

    /**
     * Type of action group.
     */
    public enum Type {
        /**
         * Load-balancing among different members in a group.
         */
        SELECT
    }

    private final PiActionGroupId id;
    private final Type type;
    private final ImmutableSet<PiActionGroupMember> members;

    private PiActionGroup(PiActionGroupId id, Type type, ImmutableSet<PiActionGroupMember> members) {
        this.id = id;
        this.type = type;
        this.members = members;
    }

    /**
     * Returns the identifier of this action group.
     *
     * @return action group identifier
     */
    public PiActionGroupId id() {
        return id;
    }

    /**
     * Returns the type of this action group.
     *
     * @return action group type
     */
    public Type type() {
        return type;
    }

    /**
     * Returns the members of this action group.
     *
     * @return collection of action members.
     */
    public Collection<PiActionGroupMember> members() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PiActionGroup that = (PiActionGroup) o;
        return id == that.id &&
                Objects.equal(type, that.type) &&
                Objects.equal(members, that.members);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, members);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", id)
                .add("type", type)
                .add("members", members)
                .toString();
    }

    /**
     * Returns a new builder of action groups.
     *
     * @return action group builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder of action groups.
     */
    public static final class Builder {

        private PiActionGroupId id;
        private Type type;
        private Map<PiActionGroupMemberId, PiActionGroupMember> members = Maps.newHashMap();

        private Builder() {
            // hides constructor.
        }

        /**
         * Sets the identifier of this action group.
         *
         * @param id action group identifier
         * @return this
         */
        public Builder withId(PiActionGroupId id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the type of this action group.
         *
         * @param type action group type
         * @return this
         */
        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Adds one member to this action group.
         *
         * @param member action group member
         * @return this
         */
        public Builder addMember(PiActionGroupMember member) {
            members.put(member.id(), member);
            return this;
        }

        /**
         * Adds many members to this action group.
         *
         * @param members action group members
         * @return this
         */
        public Builder addMembers(Collection<PiActionGroupMember> members) {
            members.forEach(this::addMember);
            return this;
        }

        /**
         * Creates a new action group.
         *
         * @return action group
         */
        public PiActionGroup build() {
            checkNotNull(id);
            checkNotNull(type);
            checkArgument(members.size() > 0, "Members cannot be empty");
            return new PiActionGroup(id, type, ImmutableSet.copyOf(members.values()));
        }
    }
}
