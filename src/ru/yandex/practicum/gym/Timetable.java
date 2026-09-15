package ru.yandex.practicum.gym;

import java.time.LocalTime;
import java.util.*;

public class Timetable {
    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            this.timetable.put(day, new TreeMap<>());
        }
    }

    public Timetable(Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable) {
        this.timetable = timetable;
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (trainingSession == null) return;

        DayOfWeek day = trainingSession.getDayOfWeek();

        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = (TreeMap<TimeOfDay, List<TrainingSession>>) timetable.computeIfAbsent(day, k -> new TreeMap<>());

        List<TrainingSession> sessionsAtTime = dayMap.computeIfAbsent(time, k -> new ArrayList<>());

        sessionsAtTime.add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {

        return (TreeMap<TimeOfDay, List<TrainingSession>>) timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        if (sessions == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(sessions);
    }

    public List<Map.Entry<String, Integer>> getCountByCoaches() {
        Map<String, Integer> coachCounts = new HashMap<>();

        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {

            for (List<TrainingSession> sessionsAtTime : daySchedule.values()) {

                for (TrainingSession session : sessionsAtTime) {
                    String coach = String.valueOf(session.getCoach());

                    coachCounts.merge(coach, 1, Integer::sum);
                }
            }
        }

        List<Map.Entry<String, Integer>> resultList = new ArrayList<>(coachCounts.entrySet());

        resultList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        return resultList;
    }
}
