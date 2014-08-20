package com.liddev.mad.teleport;

/**
 *
 * @author Renlar <liddev.com>
 */
public class Invite {

  private PlayerData invitee;
  private PlayerData inviter;
  private long creationTime;
  private JumpPoint jumpPoint;

  public Invite(PlayerData invitee, PlayerData inviter, JumpPoint jump) {
    this.invitee = invitee;
    this.inviter = inviter;
    this.creationTime = System.currentTimeMillis();
    this.jumpPoint = jump;
  }

  public PlayerData getInvitee() {
    return invitee;
  }

  public PlayerData getInviter() {
    return inviter;
  }

  public long getCreationTime() {
    return creationTime;
  }

  public JumpPoint getJumpPoint() {
    return jumpPoint;
  }
}
