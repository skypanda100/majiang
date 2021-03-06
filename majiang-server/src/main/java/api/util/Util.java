package api.util;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Util {
    public static final String SPLIT = "\t";
    public static final String TIME_FORMAT1 = "yyyy/M/d H:mm";
    public static final String TIME_FORMAT2 = "yyyy/MM/dd HH:mm";
    public static final String DATE_FORMAT = "yyyy/M/d";

    public static final long TIME_SPAN = 10 * 3600 * 1000;
    @Value("${data.path}")
    public static String MA_JIANG_PATH;

    public static Date string2Date(String dateStr, String fmt) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String date2String(Date date, String fmt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
        String dateStr = simpleDateFormat.format(date);

        return dateStr;
    }

    public static boolean isSameDay(Date prevDate, Date curDate) {
        long prevTime = prevDate.getTime();
        long curTime = curDate.getTime();
        return ((curTime - prevTime) < TIME_SPAN);
    }

    public static String array2String(String[] elements) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                stringBuffer.append(SPLIT);
            }
            stringBuffer.append(elements[i]);
        }
        return stringBuffer.toString();
    }

    public static String[] cut(String line) {
        List<String> elementList = new ArrayList<>();
        int i = -1;
        int index;
        while (i < line.length()) {
            index = line.indexOf(SPLIT, i + 1);
            if (index == -1) {
                if ((i + 1) == line.length()) {
                    elementList.add("");
                } else {
                    elementList.add(line.substring(i + 1));
                }
                break;
            } else {
                if ((index - i) == 1) {
                    elementList.add("");
                } else {
                    elementList.add(line.substring(i + 1, index));
                }
                i = index;
            }
        }
        //
        String[] array = new String[elementList.size()];
        elementList.toArray(array);

        return array;
    }

    public static boolean readMergeRecord(List<String> titleList, List<String> dataList) {
        boolean ret = true;
        FileReader mjFileReader = null;
        BufferedReader mjBufferedReader = null;
        try {
            mjFileReader = new FileReader(Util.MA_JIANG_PATH);
            mjBufferedReader = new BufferedReader(mjFileReader);
            String line;
            String[] curElements;
            int lineIndex = 0;
            Date prevDate = null;
            while ((line = mjBufferedReader.readLine()) != null) {
                lineIndex++;
                curElements = Util.cut(line);
                if (lineIndex == 1) {
                    titleList.addAll(Arrays.asList(curElements));
                }
                if (lineIndex <= 3) {
                    continue;
                }
                if (lineIndex == 4) {
                    dataList.add(line);
                } else {
                    String[] prevElements = Util.cut(dataList.get(dataList.size() - 1));
                    if (prevDate == null) {
                        prevDate = Util.string2Date(prevElements[0], Util.TIME_FORMAT1);
                    }
                    Date curDate = Util.string2Date(curElements[0], Util.TIME_FORMAT1);
                    boolean isSameDay = Util.isSameDay(prevDate, curDate);
                    if (isSameDay) {
                        prevDate = curDate;
                        for (int i = 1; i < prevElements.length; i++) {
                            String prevElement = prevElements[i];
                            String curElement = curElements[i];
                            if (!prevElement.isEmpty() && !curElement.isEmpty()) {
                                float v = Float.valueOf(prevElement) + Float.valueOf(curElement);
                                prevElements[i] = String.valueOf(v);
                            } else if (prevElement.isEmpty() && !curElement.isEmpty()) {
                                float v = Float.valueOf(curElement);
                                prevElements[i] = String.valueOf(v);
                            } else if (!prevElement.isEmpty() && curElement.isEmpty()) {
                                float v = Float.valueOf(prevElement);
                                prevElements[i] = String.valueOf(v);
                            } else {
                                //
                            }
                            dataList.set(dataList.size() - 1, Util.array2String(prevElements));
                        }
                    } else {
                        prevDate = null;
                        dataList.add(Util.array2String(curElements));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        } finally {
            if (mjFileReader != null) {
                try {
                    mjFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mjBufferedReader != null) {
                try {
                    mjBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    public static boolean readSingleRecord(List<String> titleList, List<String> dataList) {
        boolean ret = true;
        FileReader mjFileReader = null;
        BufferedReader mjBufferedReader = null;
        try {
            mjFileReader = new FileReader(Util.MA_JIANG_PATH);
            mjBufferedReader = new BufferedReader(mjFileReader);
            String line;
            String[] curElements;
            int lineIndex = 0;
            while ((line = mjBufferedReader.readLine()) != null) {
                lineIndex++;
                curElements = Util.cut(line);
                if (lineIndex == 1) {
                    titleList.addAll(Arrays.asList(curElements));
                }
                if (lineIndex <= 3) {
                    continue;
                }
                if (lineIndex == 4) {
                    dataList.add(line);
                } else {
                    dataList.add(Util.array2String(curElements));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        } finally {
            if (mjFileReader != null) {
                try {
                    mjFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mjBufferedReader != null) {
                try {
                    mjBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public static boolean readRecord(boolean merge, List<String> titleList, List<String> dataList) {
        if (merge) {
            return Util.readMergeRecord(titleList, dataList);
        } else {
            return Util.readSingleRecord(titleList, dataList);
        }
    }
}
