package uk.co.mruoc.demo.security;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserGroupIdFinderTest {

    private final IdentityService identityService = mock(IdentityService.class);

    @Test
    void shouldReturnUserGroupIdsForGivenUserId() {
        String userId = "my-user-id";
        List<String> expectedGroupIds = givenGroupIdsForUser(userId);

        List<String> groupIds = UserGroupIdFinder.findUserGroupIds(userId, identityService);

        assertThat(groupIds).containsAnyElementsOf(expectedGroupIds);
    }

    private List<String> givenGroupIdsForUser(String userId) {
        List<String> groupIds = Arrays.asList("group-id-1", "group-id-2");
        GroupQuery query = mock(GroupQuery.class);
        when(identityService.createGroupQuery()).thenReturn(query);
        when(query.groupMember(userId)).thenReturn(query);
        List<Group> groups = toGroups(groupIds);
        when(query.list()).thenReturn(groups);
        return groupIds;
    }

    private List<Group> toGroups(Collection<String> groupIds) {
        return groupIds.stream()
                .map(this::givenGroupWithId)
                .collect(Collectors.toList());
    }

    private Group givenGroupWithId(String id) {
        Group group = mock(Group.class);
        when(group.getId()).thenReturn(id);
        return group;
    }

}
