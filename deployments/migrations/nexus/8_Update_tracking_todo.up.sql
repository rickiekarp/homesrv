ALTER TABLE reminders
ADD COLUMN reminder_startdate TIMESTAMP default CURRENT_TIMESTAMP AFTER reminder_interval;