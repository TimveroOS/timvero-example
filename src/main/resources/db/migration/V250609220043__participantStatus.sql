
-- tag::add-status-column[]
-- Migration: Add participant status functionality

-- Add status column to audit table (for historical tracking)
alter table if exists aud_participant 
   add column status varchar(255);

-- Add status column to main participant table
alter table if exists participant 
   add column status varchar(255);

-- Set default status for all existing participants
update participant set status = 'NEW';

-- Make status column mandatory after setting default values
alter table if exists participant 
   alter column status set not null;
-- end::add-status-column[]
