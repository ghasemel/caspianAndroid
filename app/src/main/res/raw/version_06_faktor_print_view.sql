CREATE VIEW pfaktor_print_view AS
SELECT
    m.num,
    m.faktor_date,
    m.is_synced,
    m.sync_date,
    m.create_date,
    s.scount,
    s.mcount,
    s.price,
    k.code AS kalaCode,
    k.name AS kalaName,
    k.mohtavi,
    k.vahed_a,
    k.vahed_f,
    k.mojoodi,
    p.code AS personCode,
    p.name_hesab AS personName,
    p.mobile,
    p.address,
    p.tel,
    p.mande
FROM spfaktor AS s
INNER JOIN mpfaktor AS m on s.mpfaktorId_FK = m.id
INNER JOIN kala AS k on s.kalaId_FK = k.id
INNER JOIN person AS p on p.id = m.personId_FK
ORDER BY s.id;