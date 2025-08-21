
    alter table if exists notification
       add column gateway_name varchar(255);

    alter table if exists notification_template
       add column gateway_name varchar(255);

    update notification
		set gateway_name = (
		    case "type"
		        when 'EMAIL' then 'devEmailGateway'
		        when 'LETTER' then 'devEmailGateway'
		        when 'SMS' then 'devSmsGateway'
		        when 'VOICE' then 'devVoiceGateway'
		        else null
		    end
		);

    update notification_template
        set gateway_name = (
            case "type"
                when 'EMAIL' then 'devEmailGateway'
                when 'LETTER' then 'devEmailGateway'
                when 'SMS' then 'devSmsGateway'
                when 'VOICE' then 'devVoiceGateway'
                else null
            end
        );

     update notification
        set "type" = 'PHONE' where "type" in ('SMS', 'VOICE');

     update notification_template
        set "type" = 'PHONE' where "type" in ('SMS', 'VOICE');

     update notification
        set "type" = 'EMAIL' where "type" = 'LETTER';

     update notification_template
        set "type" = 'EMAIL' where "type" = 'LETTER';

    alter table if exists notification
       alter column gateway_name set not null;

    alter table if exists notification_template
       alter column gateway_name set not null;

    alter table if exists notification_template
       alter column type drop not null;
