package uk.co.mruoc.demo.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupQueryMother {

    public static GroupQuery groupQueryWithIdsForUser(String userId, Collection<String> groupIds) {
        GroupQuery query = mock(GroupQuery.class);
        when(query.groupMember(userId)).thenReturn(query);
        List<Group> groups = toGroups(groupIds);
        when(query.list()).thenReturn(groups);
        return query;
    }

    private static List<Group> toGroups(Collection<String> groupIds) {
        return groupIds.stream()
                .map(GroupQueryMother::givenGroupWithId)
                .toList();
    }

    private static Group givenGroupWithId(String id) {
        Group group = mock(Group.class);
        when(group.getId()).thenReturn(id);
        return group;
    }

}
