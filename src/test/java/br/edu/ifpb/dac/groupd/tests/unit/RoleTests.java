package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.Assert.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.commons.annotation.Testable;

import br.edu.ifpb.dac.groupd.business.service.RoleService;


@Testable
@DisplayName("Role")
public class RoleTests {

    @Test
    public void testIfRoleIsAdmin() {
        RoleService.AVAILABLE_ROLES roleService = RoleService.AVAILABLE_ROLES.ADMIN;
        assertEquals("Admin", roleService.getRole());
    }

    @Test
    public void testIfRoleIsNotAdmin() {
        RoleService.AVAILABLE_ROLES roleService = RoleService.AVAILABLE_ROLES.USER;
        assertEquals("User", roleService.getRole());
    }

    @Test
    public void testInput() {
        RoleService.AVAILABLE_ROLES roleService = RoleService.AVAILABLE_ROLES.USER;
        assertNotEquals(RoleService.AVAILABLE_ROLES.USER, roleService.getRole());
        assertNotEquals(null, roleService.getRole());
    }

}
