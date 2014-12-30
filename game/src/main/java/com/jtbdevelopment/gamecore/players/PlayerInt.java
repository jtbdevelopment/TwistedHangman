package com.jtbdevelopment.gamecore.players;

import org.springframework.data.annotation.Transient;

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
public interface PlayerInt<ID> {

    ID getId();

    void setId(final ID id);

    @Transient
    String getIdAsString();

    String getSource();

    void setSource(final String source);

    String getSourceId();

    void setSourceId(final String sourceId);

    String getDisplayName();

    void setDisplayName(final String displayName);

    String getImageUrl();

    void setImageUrl(String imageUrl);

    String getProfileUrl();

    void setProfileUrl(String profileUrl);

    boolean getDisabled();

    boolean isDisabled();

    void setDisabled(boolean disabled);

    boolean getAdminUser();

    boolean isAdminUser();

    void setAdminUser(boolean adminUser);

    String getMd5();
}
