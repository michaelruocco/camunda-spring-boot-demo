package uk.co.mruoc.demo.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGroupIdFinder {

    public static List<String> findUserGroupIds(String userId, IdentityService identityService) {
        return identityService.createGroupQuery()
                .groupMember(userId)
                .list()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }

}
