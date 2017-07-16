package project.dao.stub.util;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public enum IdGenerator {

    BIG_INTEGER {
        /**
         * Return BigInteger from random UUID
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T> T generateId() {
            BigInteger result = getBigIntegerFromUuid(UUID.randomUUID());
            return (T) result;
        }

        private BigInteger getBigIntegerFromUuid(UUID randomUUID) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(randomUUID.getMostSignificantBits());
            bb.putLong(randomUUID.getLeastSignificantBits());
            return new BigInteger(1, bb.array());
        }
    };

    public abstract <T> T generateId();
}
