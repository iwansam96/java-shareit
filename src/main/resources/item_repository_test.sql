insert into users (user_id, user_name, user_email) values
    (1, 'user1', 'user1@mail.me'),
    (2, 'user2', 'user2@email.re');

insert into items (item_id, item_name, item_description, item_available, item_owner) values
    (1, 'item1', 'item1 original description', true, 1);

insert into booking_statuses (booking_status_id, booking_status_name) values
    (1, 'APPROVED'),
    (2, 'CANCELLED');

insert into bookings (booking_id, booking_start, booking_end, item, booker, booking_status) values
    (1, now() - 1, now() + 1, 1, 2, 1),
    (2, now() + 1, now() + 2, 1, 2, 1);