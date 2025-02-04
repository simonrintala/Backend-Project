# Säkerhetsanalysmall för Spring Boot-projekt

## 1. Projektöversikt

- Beskriv kort vad er applikation gör

- Lista huvudfunktionaliteter

- Identifiera vilka användare/roller som finns i systemet

## 2. Känslig Data

### 2.1 Identifiera känslig information

Kryssa i det som stämmer för er, fyll på med fler om det behövs.

- [ ] Personuppgifter
- [ ] Inloggningsuppgifter
- [ ] Betalningsinformation
- [ ] Annan känslig affärsdata

### 2.2 Dataskyddsåtgärder

Beskriv hur du skyddar den känsliga informationen:

- Kryptering (vilken data krypteras och hur?)

- Säker datalagring

- Säker dataöverföring

## 3. Autentisering & Auktorisering

### 3.1 Inloggningssäkerhet

Kryssa i det som finns med/det ni har hanterar eller ska hantera i er applikation

- [ ] Lösenordskrav (längd, komplexitet)
- [ ] Hantering av misslyckade inloggningsförsök
- [ ] Session hantering
- [ ] JWT/Token säkerhet

### 3.2 Behörighetskontroll

Kryssa i det som finns med/det ni har hanterat eller ska hantera i er applikation

- [ ] Olika användarnivåer/roller
- [ ] Åtkomstkontroll för endpoints
- [ ] Validering av användarrättigheter

## 4. API Säkerhet

### 4.1 Input Validering

Kryssa i det som finns med/det ni har hanterar eller ska hantera i er applikation. Kryssa i även om vissa är disabled men skriv inom parentes disabled

- [ ] Validering av alla användarinput
- [ ] Skydd mot SQL Injection
- [ ] Skydd mot XSS
- [ ] Skydd mot CSRF

### 4.2 API Endpoints

Kryssa i det som finns med/det ni har hanterat eller ska hantera i er applikation

- [ ] HTTPS användning
- [ ] Rate limiting
- [ ] CORS konfiguration
- [ ] Error handling (inga känsliga felmeddelanden)

## 5. Implementerade Säkerhetsåtgärder

Plocka ut det delar ur Spring Security som ni tycker är viktigast, räcker med ett par kod snuttar.
Lista konkreta säkerhetsimplementeringar:

```java
// Exempel på säkerhetskonfiguration
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Din säkerhetskonfiguration här
}
```

## 6. Kvarstående Risker

OBS! Kan fyllas i mot slutet av projektet

- Lista kända säkerhetsrisker som behöver åtgärdas
- Förslag på framtida förbättringar

## Tips för genomförande

1. Börja med att identifiera känslig data i systemet
2. Fokusera på de viktigaste säkerhetsaspekterna först
3. Dokumentera säkerhetsval och motivera dem

## Vanliga Spring Security-komponenter att överväga

- SecurityFilterChain
- UserDetailsService
- PasswordEncoder
- JwtAuthenticationFilter
