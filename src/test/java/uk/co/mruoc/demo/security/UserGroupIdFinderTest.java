package uk.co.mruoc.demo.security;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.GroupQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
        GroupQuery query = GroupQueryMother.groupQueryWithIdsForUser(userId, groupIds);
        when(identityService.createGroupQuery()).thenReturn(query);
        return groupIds;
    }

}
