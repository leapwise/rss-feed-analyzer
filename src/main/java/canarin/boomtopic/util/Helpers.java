package canarin.boomtopic.util;

public class Helpers {
    /**
     * This method generates random ID for analysed data
     *
     * @return the random generated ID
     */
    public static long generateRandomID() {
        return (long) (Math.random() * 10000000);
    }
}
