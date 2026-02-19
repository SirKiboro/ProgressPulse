# Progress Pulse — Production System Architecture (Implementation-Ready)

## 1) High-Level System Architecture

### 1.1 Architectural Style
Use a **modular monolith first** (Spring Boot 3.x, Java 21), with explicit domain boundaries and async event contracts so it can be split into microservices when scale or team topology demands it.

**Why this shape now**
- Faster delivery for MVP with fewer operational burdens.
- Easier transactional consistency for workout/program/trainer flows.
- Lower cloud cost during free-tier phase.
- Keeps clear extraction seams for future services (tracking, messaging, billing).

### 1.2 Core Modules (inside one Spring Boot codebase)
1. **Identity & Access**
   - AuthN/AuthZ, JWT issuing/validation, role/permission checks.
2. **Tenant & Gym Management**
   - Gym onboarding, branding, memberships, tenant isolation policy.
3. **Workout & Program Engine**
   - Exercise catalog, templates, sessions, set logging, PR analytics.
4. **Outdoor Activity Tracking**
   - Live GPS ingestion, route simplification, metrics, records.
5. **Trainer-Client Management**
   - Assignments, plans, progress dashboards, messaging.
6. **Analytics & Reporting**
   - KPIs for user/trainer/gym.
7. **Feature Flags & Monetization Readiness**
   - Plan features, entitlement checks, billing adapters.
8. **Notification & Messaging**
   - In-app thread messages, push notification orchestration.

### 1.3 Runtime Components
- **API Gateway / Ingress** (WAF + TLS termination + rate limits).
- **Spring Boot API** pods (stateless).
- **PostgreSQL** primary + read replica.
- **Redis** for cache/session revocation/rate-limiting counters.
- **Object storage** (e.g., S3-compatible) for media and exported reports.
- **Message broker** (RabbitMQ or Kafka; RabbitMQ sufficient in MVP).
- **Observability stack** (OpenTelemetry, Prometheus, Grafana, Loki/ELK).

### 1.4 Primary Request Flows
- Gym app/API calls REST endpoints with JWT.
- Live outdoor sessions push GPS points over WebSocket.
- Tracking module writes raw points asynchronously and emits `ActivityUpdated` events.
- Analytics module computes aggregate metrics and writes summary projections.

---

## 2) Technology Stack Justification

## Backend
- **Java 21 + Spring Boot 3.3+**: mature ecosystem, enterprise talent availability, security features, virtual threads optional for high concurrency.
- **Spring Modules**: Web, Security, Data JPA, Validation, WebSocket, Actuator.

## Persistence
- **PostgreSQL 16 + PostGIS**:
  - Strong relational guarantees for trainer/workout/business logic.
  - PostGIS enables geospatial route queries (distance-on-earth, bounding boxes).
  - JSONB for flexible metadata (device info, localized attributes).

## Caching + Real-Time support
- **Redis**:
  - Live session state cache, token blacklist/rotation lists, throttling counters.

## Messaging
- **RabbitMQ** (MVP) with routing keys:
  - `tracking.point.ingested`, `workout.session.completed`, `program.assigned`, etc.
  - Later migration path to Kafka for very high telemetry throughput.

## Frontend (suggested)
- **React/Next.js** web app + optional React Native companion for richer GPS capture.
- Map rendering via **Mapbox GL JS** or **Leaflet + OpenStreetMap**.

## Maps Provider decision
- Start with **OpenStreetMap + Mapbox Tiles** for cost control and regional flexibility.
- Keep provider abstraction interface (`MapProviderService`) for future Google Maps enterprise agreements.

## Kenyan market integration
- Payment abstraction with placeholder **`PaymentProvider` SPI** and `MpesaProvider` stub.
- Locale-ready formatting (`sw-KE`, `en-KE`) and EAT timezone default.

---

## 3) Database Schema Design (Core Tables + Relationships)

> All tenant-scoped tables include `tenant_id UUID NOT NULL` and are indexed with business keys.

### 3.1 Identity & Tenant
- `tenants(id, name, slug, status, created_at)`
- `gyms(id, tenant_id, name, branch_name, country_code, city, timezone, branding_jsonb, created_at)`
- `users(id, tenant_id, email, phone_e164, password_hash, status, locale, created_at)`
- `roles(id, tenant_id nullable, code)` (`SUPER_ADMIN`, `GYM_ADMIN`, `TRAINER`, `CLIENT`)
- `permissions(id, code)`
- `role_permissions(role_id, permission_id)`
- `user_roles(user_id, role_id)`

### 3.2 Membership & Commercial
- `membership_plans(id, tenant_id, code, name, tier, duration_days, active)`
- `client_memberships(id, tenant_id, user_id, gym_id, membership_plan_id, start_at, end_at, status)`
- `feature_flags(id, code, description)`
- `plan_features(id, membership_plan_id, feature_flag_id, enabled, limits_jsonb)`
- `subscriptions(id, tenant_id, gym_id, plan_code, status, trial_ends_at, next_billing_at)` (future billing)

### 3.3 Workout & Programs
- `exercises(id, tenant_id nullable, name, category, muscle_group, equipment, instructions, is_global, locale)`
- `workout_templates(id, tenant_id, created_by, name, goal_type, visibility)`
- `workout_template_items(id, template_id, exercise_id, sequence_no, target_sets, target_reps, target_rpe, rest_seconds)`
- `programs(id, tenant_id, trainer_id, name, duration_weeks, difficulty, objective)`
- `program_weeks(id, id_program, week_no)`
- `program_sessions(id, week_id, day_of_week, template_id, notes)`
- `program_assignments(id, tenant_id, program_id, trainer_id, client_id, assigned_at, start_date, status)`
- `workout_sessions(id, tenant_id, user_id, gym_id, template_id nullable, program_assignment_id nullable, started_at, ended_at, attendance_status)`
- `workout_sets(id, session_id, exercise_id, set_no, reps, weight_kg, rpe, completed_at)`
- `personal_records(id, tenant_id, user_id, exercise_id, metric_type, metric_value, achieved_at, source_session_id)`

### 3.4 Outdoor Tracking
- `activities(id, tenant_id, user_id, type, source_device, started_at, ended_at, duration_sec, distance_m, elevation_gain_m, avg_pace_sec_per_km, calories_kcal, status)`
- `activity_points(id, activity_id, ts, lat, lon, altitude_m, speed_mps, heart_rate, accuracy_m)` partitioned by month for scale.
- `activity_routes(id, activity_id, geom_linestring geography, simplified_geom geography, map_provider)`
- `activity_summaries(id, activity_id, moving_time_sec, elapsed_time_sec, avg_speed_mps, max_speed_mps, splits_jsonb)`
- `activity_records(id, tenant_id, user_id, record_type, value, activity_id, achieved_at)`

### 3.5 Trainer CRM + Messaging
- `trainer_clients(id, tenant_id, trainer_id, client_id, status, started_at)`
- `checkins(id, tenant_id, trainer_id, client_id, channel, message, due_at, completed_at)`
- `conversations(id, tenant_id, gym_id, created_at)`
- `conversation_participants(conversation_id, user_id)`
- `messages(id, conversation_id, sender_id, body, sent_at, read_at)`

### 3.6 Reporting
- `trainer_metrics_daily(id, tenant_id, trainer_id, date, active_clients, completion_rate, retention_30d, avg_response_minutes)`
- `gym_metrics_daily(id, tenant_id, gym_id, date, dau, mau, avg_sessions_per_member, churn_rate, attendance_rate)`

### 3.7 Key Constraints / Indexes
- Unique `(tenant_id, email)` on users.
- Unique `(tenant_id, code)` on roles.
- GIN index on JSONB fields with query use-cases.
- PostGIS spatial index on route geometries.
- Composite index `(tenant_id, user_id, started_at desc)` for activity/workout history.

---

## 4) Multi-Tenant Design Strategy

## 4.1 Tenancy Model
Adopt **shared DB, shared schema, tenant discriminator** initially.

**Isolation controls**
- Mandatory `tenant_id` column + not-null constraints.
- Hibernate filter OR explicit repository scoping + custom `TenantContext` middleware.
- PostgreSQL **Row-Level Security (RLS)** policies for defense-in-depth.

## 4.2 Tenant resolution
- Via JWT claim `tid` and optional gym subdomain (`{gymSlug}.progresspulse.co.ke`).
- Request filter resolves tenant and sets context for all queries.

## 4.3 Migration path
- For enterprise gyms requiring hard isolation: move to **schema-per-tenant** for selected large customers using same domain contracts.

---

## 5) Real-Time GPS Tracking Architecture

## 5.1 Session lifecycle
1. Client starts activity (`POST /api/v1/activities/start`) -> activity row created with status `LIVE`.
2. Client opens WebSocket `/ws/tracking` with JWT.
3. Client streams location points every 1–5 seconds.
4. Server validates point (timestamp skew, speed sanity, geo bounds).
5. Points buffered in Redis stream and batched insert to `activity_points` every N points or 3 sec.
6. Real-time metrics published back to client (`distance`, `pace`, `elapsed`).
7. On end, route simplification (Douglas-Peucker), summary computation, PR detection.

## 5.2 Protocol details
- Message payload:
  - `{activityId, ts, lat, lon, altitude, speed, accuracy, hr}`
- ACK payload:
  - `{seq, accepted, reason?}`
- Backpressure:
  - if client over-sends, server responds with throttle hint and enforces per-session QPS cap.

## 5.3 Reliability and edge handling
- Offline mode: client stores points local queue and uploads on reconnect via bulk REST endpoint.
- Anti-cheat heuristics:
  - impossible jumps, unrealistic speed profiles, repeated GPS spoof patterns.
- Battery optimization:
  - adaptive sampling by movement speed.

---

## 6) Trainer-Client Domain Model

## 6.1 Core use-cases
- Trainer accepts client assignment.
- Trainer prescribes program template with start date.
- Client logs sessions against prescribed plan.
- Trainer sees adherence, PR changes, attendance trends.
- Bi-directional messages and check-in reminders.

## 6.2 Key APIs (representative)
- `POST /api/v1/trainers/{trainerId}/clients/{clientId}/assign`
- `POST /api/v1/programs`
- `POST /api/v1/program-assignments`
- `GET /api/v1/trainers/{trainerId}/dashboard`
- `POST /api/v1/conversations/{id}/messages`

## 6.3 Metrics for trainer performance
- Monthly retention of assigned clients.
- Program completion ratio.
- Median response SLA in in-app chat.
- Average weekly client activity count.

---

## 7) Role & Permission System

## 7.1 Role model
- `SUPER_ADMIN`: platform operations.
- `GYM_ADMIN`: tenant/gym setup, memberships, reports.
- `TRAINER`: client management, program prescription, analytics.
- `CLIENT`: workouts, activities, messaging.

## 7.2 Permission examples (fine-grained)
- `program:create`, `program:assign`, `client:view_all_gym`, `activity:track_live`, `report:gym_financial`.

## 7.3 Enforcement
- JWT includes role + permission snapshots (`roles`, `perms`, `tid`, `gid`).
- Server-side method security: `@PreAuthorize("hasAuthority('program:assign')")`.
- Tenant scope validation on every resource read/write.

---

## 8) Freemium Expansion Architecture

## 8.1 Entitlement checks
Central `EntitlementService`:
- Input: `tenantId`, `gymId`, `featureCode`.
- Output: enabled + usage limits.

## 8.2 Feature flags
- Use LaunchDarkly/Unleash or DB-driven toggles for staged rollout.
- Flags at levels: global, tenant, gym, role.

## 8.3 Planned premium candidates
- Advanced analytics exports.
- Wearable integrations.
- Multi-branch consolidated reporting.
- Automated trainer recommendations.

## 8.4 Payment readiness
- Billing adapter interface:
  - `createSubscription()`, `chargeInvoice()`, `cancelSubscription()`.
- Initial implementation: `NoOpBillingProvider`.
- Future providers: Stripe + M-Pesa.

---

## 9) Security Design

## 9.1 Authentication & session security
- Access JWT short-lived (15 min), refresh token rotation (httpOnly secure cookie for web).
- Device-bound refresh token records with revocation list in Redis.
- MFA optional for gym admins and trainers.

## 9.2 API Security
- OWASP ASVS-aligned controls.
- Input validation (Bean Validation + custom GPS bounds checks).
- SQLi protection via JPA parameterized queries.
- XSS controls on message content (allowlist sanitizer).

## 9.3 Tenant isolation
- Mandatory tenant claim verification.
- RLS policies in PostgreSQL.
- Audit log table for sensitive actions (`who`, `tenant`, `action`, `resource`, `ip`).

## 9.4 Rate limiting & abuse control
- Per-IP + per-user + per-tenant quotas (Redis token bucket).
- Stricter caps on auth endpoints and GPS ingest endpoints.

## 9.5 Data protection
- TLS 1.2+ in transit.
- Encryption at rest (managed DB + object store SSE).
- Password hashing Argon2id.
- PII retention and deletion workflows (Kenya Data Protection Act compliance alignment).

---

## 10) Deployment Architecture (Cloud-Ready)

## 10.1 Environment topology
- **Dev / Staging / Prod** isolated projects/accounts.
- Kubernetes (EKS/GKE/AKS) with Helm.
- Managed PostgreSQL + Redis.
- CDN for static assets.

## 10.2 CI/CD
- GitHub Actions pipeline:
  1. Build + unit tests.
  2. Static analysis (SpotBugs, Checkstyle, OWASP dependency-check).
  3. Container build + scan (Trivy).
  4. Deploy to staging.
  5. Smoke tests.
  6. Manual approval to prod.

## 10.3 Observability SLOs
- API p95 latency < 300ms for non-analytics endpoints.
- Activity point ingestion success > 99.9%.
- Error budget tracking and alerting policies.

---

## 11) Scalability Considerations

- **Horizontal scale** stateless API pods behind load balancer.
- **Hot partition mitigation** for telemetry tables by monthly partitions + activity_id hashing if needed.
- **Read replicas** for reporting-heavy queries.
- **Async projections** for dashboards to avoid expensive live joins.
- **Caching** frequently accessed template/exercise catalogs.

---

## 12) Risk Analysis

1. **GPS noise in dense Nairobi urban corridors**
   - Mitigate with smoothing + accuracy thresholds + map matching.
2. **Tenant data leakage risk**
   - Mitigate with tenant middleware tests + RLS + security audits.
3. **Trainer adoption friction**
   - Mitigate via simplified dashboard and predefined templates.
4. **Free-tier cost overrun**
   - Mitigate via usage caps and telemetry sampling policy.
5. **Payment integration complexity (M-Pesa)**
   - Mitigate with provider abstraction and early sandbox contract tests.

---

## 13) MVP Scope Definition (Phase 1)

Deliver in 12–16 weeks:
- Tenant-aware auth and RBAC.
- Gym onboarding + branding basics.
- Workout logging (sets/reps/weight/RPE), PR tracking.
- Trainer-client assignments + program prescription.
- Outdoor tracking (start/stop/live points/basic maps/history).
- Basic messaging threads.
- Admin reports: attendance, engagement, trainer activity.
- Entitlement framework present but only free plan enabled.

Out of MVP:
- Wearables sync, AI coaching, full billing, deep social features.

---

## 14) Phase 2 & Phase 3 Expansion Strategy

## Phase 2 (3–6 months post-MVP)
- Paid plans activation + Stripe integration + M-Pesa pilot.
- Advanced analytics (cohort retention, funnel by gym branch).
- Push notifications and scheduled nudges.
- Data warehouse sync (BigQuery/Snowflake) for BI.

## Phase 3 (6–12 months)
- Selective microservice extraction (tracking + messaging).
- Marketplace model for independent trainers.
- Corporate wellness tenant packages.
- AI-assisted plan progression recommendations.

---

## 15) Trade-offs and Implementation Notes

- **Modular monolith vs microservices**: monolith wins on delivery speed and lower ops overhead now; event-driven boundaries preserve future split option.
- **PostgreSQL/PostGIS vs NoSQL**: relational consistency needed for commercial gym workflows; geospatial extension handles route needs without introducing extra datastores early.
- **WebSocket vs polling for live tracking**: WebSocket gives lower latency and battery/network efficiency; requires connection management and backpressure logic.
- **Shared-schema multi-tenancy**: cheapest and quickest at start but demands strict tenant guardrails and compliance posture.
- **OpenStreetMap stack vs Google Maps**: lower early cost and more control; Google may offer richer geocoding/places at higher cost.

### Edge Cases to Handle Explicitly
- Client switches networks mid-activity -> resume same activity stream with idempotent point upsert key (`activity_id + ts`).
- Duplicate point submissions -> dedupe by unique constraint.
- Trainer removed while active assignments exist -> reassignment workflow with audit trail.
- Membership expired mid-month -> grace-period policy and entitlement downgrade event.
- Clock skew from low-end devices -> server timestamp normalization with bounded tolerance.

---

## 16) Suggested Initial Package Layout (Spring Boot)

```text
com.progresspulse.app
  ├─ identity
  ├─ tenant
  ├─ gym
  ├─ workout
  ├─ tracking
  ├─ trainer
  ├─ messaging
  ├─ analytics
  ├─ monetization
  ├─ shared (security, exceptions, events, util)
```

This package layout aligns with future extraction into deployable services while keeping a coherent monolith for MVP.
