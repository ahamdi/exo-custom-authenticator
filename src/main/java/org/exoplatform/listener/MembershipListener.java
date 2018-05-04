package org.exoplatform.listener;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;

/**
 * Created by eXo Platform SAS.
 *
 * @author Ali Hamdi <ahamdi@exoplatform.com>
 * @since 03/05/18 18:00
 */

public class MembershipListener extends MembershipEventListener {
  private static final Log log = ExoLogger.getLogger(MembershipListener.class);

  @Override
  public void preSave(Membership m, boolean isNew) throws Exception {
    log.warn("Saving membership "+m.getMembershipType()+" for user "+m.getUserName()+" in group "+m.getGroupId());
  }

  @Override
  public void postSave(Membership m, boolean isNew) throws Exception {
    log.warn("Membership "+m.getMembershipType()+" for user "+m.getUserName()+" in group "+m.getGroupId()+" is saved !");
  }

  @Override
  public void preDelete(Membership m) throws Exception {
    log.warn("Deleting membership "+m.getMembershipType()+" for user "+m.getUserName()+" in group "+m.getGroupId());
  }

  @Override
  public void postDelete(Membership m) throws Exception {
    log.warn("Membership "+m.getMembershipType()+" for user "+m.getUserName()+" in group "+m.getGroupId()+" is deleted !");
  }
}
