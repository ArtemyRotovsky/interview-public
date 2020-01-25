package com.devexperts.account;

import lombok.*;

/**
 * Unique Account identifier
 *
 * <p>
 * NOTE: we suspect that later {@link #accountId} is not going to be uniquely identifying an account,
 * as we might add human-readable account representation and some clearing codes for partners.
 * */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class AccountKey {
    private long accountId;

    public static AccountKey valueOf(long accountId) {
        return new AccountKey(accountId);
    }

}
