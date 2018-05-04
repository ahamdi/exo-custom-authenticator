package org.exoplatform.authenticator;

import javax.security.auth.login.LoginException;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.auth.OrganizationAuthenticatorImpl;
import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.PasswordEncrypter;
import org.exoplatform.services.security.RolesExtractor;

/**
 * Created by eXo Platform SAS.
 *
 * @author Ali Hamdi <ahamdi@exoplatform.com>
 * @since 03/05/18 19:02
 */

public class CustomAuthenticator extends OrganizationAuthenticatorImpl {

  protected static final Log LOG = ExoLogger.getLogger(CustomAuthenticator.class);
  private static final String MEMBER = "member";
  private static final String PLATFORM_USERS = "/platform/users";

  public CustomAuthenticator(OrganizationService orgService, RolesExtractor rolesExtractor, PasswordEncrypter encrypter) {
    super(orgService, rolesExtractor, encrypter);
  }

  public CustomAuthenticator(OrganizationService orgService, RolesExtractor rolesExtractor) {
    super(orgService, rolesExtractor);
  }

  public CustomAuthenticator(OrganizationService orgService) {
    super(orgService);
  }

  @Override
  public Identity createIdentity(String userId) throws Exception {
    Identity userIdentity = super.createIdentity(userId);
    if(userIdentity.getMemberships().isEmpty()){
      addUserToPlatformUsers(userId);
    }
    return super.createIdentity(userId);
  }

  @Override
  public String validateUser(Credential[] credentials) throws LoginException, Exception {
    return super.validateUser(credentials);
  }

  /**
   * Add given user to our group with given membershipType.
   *
   * @param userId
   */
  private void addUserToPlatformUsers(String userId) throws Exception {
    OrganizationService orgService = (OrganizationService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(
        OrganizationService.class);
    try {
      begin(orgService);
      User user = orgService.getUserHandler().findUserByName(userId);
      MembershipType memberType = orgService.getMembershipTypeHandler().findMembershipType(MEMBER);
      Group platformUsersGroup = orgService.getGroupHandler().findGroupById(PLATFORM_USERS);
      orgService.getMembershipHandler().linkMembership(user, platformUsersGroup, memberType, true);
    } catch (Exception e) {
      LOG.error("Failed to add user " + userId + " to group " + PLATFORM_USERS + ".", e);
      // don't rethrow login exception in case of failure.
      // throw e;
    } finally {
      end(orgService);
    }
  }
}
