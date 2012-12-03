package gov.nbcs.rp.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Random;

/**
 * 生成UUID的工具类，由java5.0的SDK修改而来供jdk1.3以后的JDK使用
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class UUID {

    private final long mostSigBits;

    private final long leastSigBits;

    private transient int version = -1;

    private transient int variant = -1;

    private transient volatile long timestamp = -1;

    private transient int sequence = -1;

    private transient long node = -1;

    private transient int hashCode = -1;
    
    /**
     * 种子缓冲，如果有两个UUID的生成使用了同一时间，
     * 则其中一个使用该时间产生的伪随机数
     */
    private static long currentSeed = -1;
    
    private static Random seedRandom;
    
    private static String netIntf="";
    
    /**
     * 加载网络接口卡的信息
     */
    static {
        try {
            for(Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            enumeration.hasMoreElements();) {
                NetworkInterface nwi = (NetworkInterface)enumeration.nextElement();
                netIntf+=nwi.getDisplayName();
                for(Enumeration enu = nwi.getInetAddresses();enu.hasMoreElements();) {
                    InetAddress ia = (InetAddress)enu.nextElement();
                    netIntf += ia.getHostAddress()+ia.getHostName();
                }
            }
        }catch(Exception ex) {
        }
    }

    private UUID(byte[] data) {
        long msb = 0;
        long lsb = 0;
        for (int i=0; i<8; i++) {
			msb = (msb << 8) | (data[i] & 0xff);
		}
        for (int i=8; i<16; i++) {
			lsb = (lsb << 8) | (data[i] & 0xff);
		}
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }

    public UUID(long mostSigBits, long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    /**
     * 生成随机的UUID
     * @return UUID对象
     */
    public static UUID randomUUID() {    
        long currentTime = System.currentTimeMillis();
        long hashSeed = currentTime;
        if(hashSeed==currentSeed) {//如果已经有当前时间被使用
            if(seedRandom==null) {
				seedRandom = new Random(hashSeed);
			}
            hashSeed = seedRandom.nextLong();//则使用当前时间产生的伪随机数
        }
        if(seedRandom!=null) {
			seedRandom.setSeed(hashSeed);
		}
        currentSeed = currentTime;
        byte[] randomBytes = (netIntf+String.valueOf(hashSeed)).getBytes();
        return UUID.nameUUIDFromBytes(randomBytes);
    }

    /**
     * 利用MD5算法生成UUID
     * @return UUID对象
     */
    public static UUID nameUUIDFromBytes(byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported");
        }
        byte[] md5Bytes = md.digest(name);
        md5Bytes[6]  &= 0x0f;  /* clear version        */
        md5Bytes[6]  |= 0x30;  /* set to version 3     */
        md5Bytes[8]  &= 0x3f;  /* clear variant        */
        md5Bytes[8]  |= 0x80;  /* set to IETF variant  */
        return new UUID(md5Bytes);
    }

    public static UUID fromString(String name) {
        String[] components = name.split("-");
        if (components.length != 5) {
			throw new IllegalArgumentException("Invalid UUID string: "+name);
		}
        for (int i=0; i<5; i++) {
			components[i] = "0x"+components[i];
		}

        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();

        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();

        return new UUID(mostSigBits, leastSigBits);
    }

    public long getLeastSignificantBits() {
        return leastSigBits;
    }

    public long getMostSignificantBits() {
        return mostSigBits;
    }

    public int version() {
        if (version < 0) {
            // Version is bits masked by 0x000000000000F000 in MS long
            version = (int)((mostSigBits >> 12) & 0x0f);
        }
        return version;
    }

    public int variant() {
        if (variant < 0) {
            // This field is composed of a varying number of bits
            if ((leastSigBits >>> 63) == 0) {
                variant = 0;
            } else if ((leastSigBits >>> 62) == 2) {
                variant = 2;
            } else {
                variant = (int)(leastSigBits >>> 61);
            }
        }
        return variant;
    }

    public long timestamp() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
        long result = timestamp;
        if (result < 0) {
            result = (mostSigBits & 0x0000000000000FFFL) << 48;
            result |= ((mostSigBits >> 16) & 0xFFFFL) << 32;
            result |= mostSigBits >>> 32;
            timestamp = result;
        }
        return result;
    }

    public int clockSequence() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
        if (sequence < 0) {
            sequence = (int)((leastSigBits & 0x3FFF000000000000L) >>> 48);
        }
        return sequence;
    }

    public long node() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
        if (node < 0) {
            node = leastSigBits & 0x0000FFFFFFFFFFFFL;
        }
        return node;
    }
    
    public String toString() {
    return "{"+(digits(mostSigBits >> 32, 8) + "-" +
        digits(mostSigBits >> 16, 4) + "-" +
        digits(mostSigBits, 4) + "-" +
        digits(leastSigBits >> 48, 4) + "-" +
        digits(leastSigBits, 12))+"}";
    }
    
    public String md5Code() {
        return (digits(mostSigBits >> 32, 8) + 
                digits(mostSigBits >> 16, 4) + 
                digits(mostSigBits, 4) + 
                digits(leastSigBits >> 48, 4) + 
                digits(leastSigBits, 12));
    }
    
    private static String digits(long val, int digits) {
    long hi = 1L << (digits * 4);
    return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
    
    public int hashCode() {
        if (hashCode == -1) {
            hashCode = (int)((mostSigBits >> 32) ^
                             mostSigBits ^
                             (leastSigBits >> 32) ^
                             leastSigBits);
        }
        return hashCode;
    }

    public boolean equals(Object obj) {
    if (!(obj instanceof UUID)) {
		return false;
	}
        if (((UUID)obj).variant() != this.variant()) {
			return false;
		}
        UUID id = (UUID)obj;
    return ((mostSigBits == id.mostSigBits) &&
                (leastSigBits == id.leastSigBits));
    }

    public int compareTo(UUID val) {
        // The ordering is intentionally set up so that the UUIDs
        // can simply be numerically compared as two numbers
        return (this.mostSigBits < val.mostSigBits ? -1 : 
                (this.mostSigBits > val.mostSigBits ? 1 :
                 (this.leastSigBits < val.leastSigBits ? -1 :
                  (this.leastSigBits > val.leastSigBits ? 1 :
                   0))));
    }

    private void readObject(java.io.ObjectInputStream in)
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();

        // Set "cached computation" fields to their initial values
        version = -1;
        variant = -1;
        timestamp = -1;
        sequence = -1;
        node = -1;
        hashCode = -1;
    }

    public static void main(String args[]) {
    	String sArrayValue[] = Common.splitString("({sdf（所担负）fs}+{sd})*2");
    	System.out.println(sArrayValue[0]);
    	System.out.println(sArrayValue[1]);
        String st = "as.(ds)";
        System.out.println(st.replaceAll("\\(|\\)","2|3"));
    }
}
