package com.konopka.calendar.service;


import com.konopka.calendar.utils.CalendarUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CalendarService {

    public File createNewCalendarFile(List<String> dates, List<String> summaries, int month) throws IOException {
        File file = new File(CalendarUtils.FILE_NAME);
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(CalendarUtils.CAL_BEGIN);
        bufferedWriter.write(CalendarUtils.VERSION);
        bufferedWriter.write(CalendarUtils.PROD_ID);
        bufferedWriter.write(CalendarUtils.CAL_SCALE);
        bufferedWriter.write(CalendarUtils.METHOD);

        writeFile(dates, summaries, month, bufferedWriter);

        bufferedWriter.write(CalendarUtils.CAL_END);
        bufferedWriter.close();

        return file;
    }

    private void writeFile(List<String> dates, List<String> summaries, int month, BufferedWriter bufferedWriter) throws IOException {
        for (int i = 0; i < dates.size(); i++) {
            bufferedWriter.write(CalendarUtils.EVENT_BEGIN);
            if (dates.get(i).length() == 1) {
                bufferedWriter.write(CalendarUtils.DATE_START + month + "0" + dates.get(i) + "\r\n");
                bufferedWriter.write(CalendarUtils.DATE_END + month + "0" +dates.get(i) + "\r\n");
            }
            else {
                bufferedWriter.write(CalendarUtils.DATE_START + month + dates.get(i) + "\r\n");
                bufferedWriter.write(CalendarUtils.DATE_END + month +dates.get(i) + "\r\n");
            }
            bufferedWriter.write(CalendarUtils.SUMMARY + summaries.get(i) + "\r\n");
            bufferedWriter.write(CalendarUtils.EVENT_END);
        }
    }

}
