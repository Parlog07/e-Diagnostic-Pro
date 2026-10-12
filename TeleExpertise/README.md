# TeleExpertise

A small Java 17 medical web application for learning Servlets, JSP, JPA and layered architecture. The interface is in French. All demo data is fictional.

Start with **[docs/setup-guide.md](docs/setup-guide.md)**. For the evaluation, read [docs/project-explanation.md](docs/project-explanation.md) and [docs/demo-plan.md](docs/demo-plan.md). Actual validation results are in [docs/verification.md](docs/verification.md).

## Implemented scope

- BCrypt login/logout, session identity, role filters, CSRF on POST forms, escaped JSP output, protected views and record ownership checks.
- Nurse: search by name or social-security number, register administrative/medical data plus vital signs atomically, today's arrivals in arrival order, record details, new arrival for an existing patient after the previous consultation is closed.
- General practitioner: waiting list, patient record, create a 150 DH consultation, save observations/diagnosis/treatment, add priced medical acts, close directly, see total and specialist advice.
- Specialist: edit specialty/tariff, see assigned requests, filter by status/priority with Streams, inspect associated medical data, provide an opinion and recommendations once.
- Expertise: search specialty and sort tariff with Streams; assign one specialist, ask a question, set priority, receive written advice, then close the consultation as the general practitioner.
- PostgreSQL schema/seed scripts, Hibernate schema validation, a small JUnit/Mockito test suite, PlantUML class diagram.

This is the deliberately reduced scope requested by the student. A precise percentage of the original brief is not asserted because features vary in size. The original medical brief was read; the embedded WeGrow text was excluded following the user's clarification.

## Omitted scope

No scheduling, slot reservation, availability display, slot cancellation or automatic archiving. `TimeSlot` and `TimeSlotStatus` are mapped learning entities and an empty table only; there is no scheduling UI. No video, phone integration, chat, email/SMS, WebSockets, medical PDFs, advanced reports/statistics, admin UI, complex audit trail, external APIs, or complex filters. Staff is created through SQL.

## Business choices

- A patient has many consultations and vital records. Their latest arrival determines whether they appear in today's list. Older vital records remain stored.
- A patient cannot rejoin the waiting list while waiting or while any consultation is open. A database lock serializes competing arrivals/consultation creation.
- A consultation has **zero or one** expertise request; `expertise_request.consultation_id` is unique. No second specialist opinion is supported in this version.
- Expertise follows `EN_ATTENTE -> TERMINEE`; consultation follows `EN_COURS -> EN_ATTENTE_AVIS_SPECIALISTE -> AVIS_RECU -> TERMINEE`, or directly `EN_COURS -> TERMINEE`.
- Total = fixed 150 DH + medical-act prices + the assigned expertise fee. The fee is included as soon as a request is created. The fee is a saved snapshot; changing a specialist's tariff cannot change existing consultation totals. Completing a request does not add another fee.
- A waiting consultation cannot be closed until its opinion is received. A completed consultation cannot be edited. Closing requires diagnosis and treatment.
- Prices use `BigDecimal` and two decimals. Vitals validation checks input format/range only; it does not give medical advice.
- Nurses access patient intake records; general practitioners access waiting patients and their own consultations; specialists access only requests assigned to them and the linked consultation. General practitioners do not receive other doctors' consultation details. Shared patient antecedents and vital history are available within an authorized care workflow.
- Emails and social-security numbers are unique in SQL. There is no user-registration endpoint; demo emails are normalized to lowercase.

## Architecture

`servlet -> service -> repository -> JPA/Hibernate -> PostgreSQL`

- `entity`: simple persistent classes with getters/setters, JOINED inheritance for `User`.
- `enums`: workflow states and priority.
- `repository`: JPA queries and persistence using an EntityManager passed by the service.
- `service`: validation, ownership and transaction boundaries; constructors accepting an EntityManagerFactory enable tests.
- `servlet`: reads HTTP parameters, calls services, forwards to protected JSP pages; successful POSTs redirect.
- `filter`: session role checks, CSRF checks, UTF-8, response headers.
- `util`: database lifecycle, connection pool, session identity, validation and CSRF helpers.
- `WEB-INF/views`: JSTL pages; no direct browser access and no queries.

Schema is managed exclusively by `database/schema.sql`, then `database/seed.sql` on an empty database. Hibernate uses **validate**, never `update`. SQL transactions protect script execution. Do not rerun the seed on a populated database; do not drop a database containing data you need.

## Build

```bash
mvn clean package
mvn test
```

Output: `target/teleexpertise.war`. Deploy on Tomcat 10.1 with Java 17. No Spring, Lombok, MapStruct, or JavaScript framework. Bootstrap 5.3.8 CSS is bundled locally; the interface works without a CDN connection.

Database variables: `TE_DB_URL`, `TE_DB_USER`, `TE_DB_PASSWORD`, set in Tomcat's local `bin/setenv.sh`. No database password is committed. Session stores only ID/name/role, never a password hash. This project is a local student demonstration with development-only accounts, not a production medical system.

## Project links

Git repository: to be supplied by the student.
Jira board: to be supplied by the student.

## Primary technical references

- Tomcat 10.1 / Servlet 6.0 / JSP 3.1: https://tomcat.apache.org/tomcat-10.1-doc/
- Hibernate 6.6 / Jakarta Persistence 3.1: https://docs.hibernate.org/orm/6.6/userguide/html_single/
- Servlet API: https://jakarta.ee/specifications/servlet/6.0/apidocs/
- JSTL: https://jakarta.ee/specifications/tags/3.0/
- PostgreSQL: https://www.postgresql.org/docs/current/
- Bootstrap source/license: https://github.com/twbs/bootstrap/blob/v5.3.8/LICENSE
