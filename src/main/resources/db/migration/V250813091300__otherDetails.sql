
    update entity_document
    set details = 'Old Other document'
    where document_type = 'OTHER';

    update aud_entity_document
    set details = 'Old Other document'
    where document_type = 'OTHER';
