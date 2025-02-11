# Säkerhetsanalysmall för Spring Boot-projekt

## 1. Projektöversikt

- Beskriv kort vad er applikation gör

Platform för privatpersoner att hyra ut och boka boenden av varandra.

- Lista huvudfunktionaliteter (MVP)
1. Skapa användare med password och username
2. Lägga upp annonser m. pris och datum enligt hostens preferenser
3. En annons måste innehålla: datumn, pris, kapacitet, location, titel, host.
4. Boka andra användares annonser, under tillgängliga datum som inloggad användare
5. Logga in med användaruppgifter

- Identifiera vilka användare/roller som finns i systemet
1. Guest (inte inloggad)
2. User (inloggad användare)
3. Host (person/bank id-verifierad användare som får lägga upp boendeannonser)
4. Admin
Note: role är en lista av enum, alla som har host har även user, och admin har även user + host.

## 2. Känslig Data

### 2.1 Identifiera känslig information

Kryssa i det som stämmer för er, fyll på med fler om det behövs.

- [X] Personuppgifter (tel., adress, id etc)
- [X] Inloggningsuppgifter (password, etc)
- [X] Betalningsinformation (betalkort, annat)
- [ ] Annan känslig affärsdata

### 2.2 Dataskyddsåtgärder

Beskriv hur du skyddar den känsliga informationen:

- Kryptering (vilken data krypteras och hur?)
1. Password requirements (längd, innehåll (t.ex. stor bokstav, siffra, etc.))
2. Password hashas och saltas med Spring Security (BCryptEncoder, 12 säkerhet)

- Säker datalagring
1. Authentication token med lagom lång expiry
2. Role checks, för att users inte ska komma åt t.ex. admin-privliged info eller ändra i db
3. Inte pusha upp känslig info på github

- Säker dataöverföring
1. Verifiera Http-request validity
2. White-list på allowed files/file-extensions (neka andra filformat)
3. Signaturbaserad filkontroll (???)

## 3. Autentisering & Auktorisering

### 3.1 Inloggningssäkerhet

Kryssa i det som finns med/det ni har hanterar eller ska hantera i er applikation

- [X] Lösenordskrav (längd, komplexitet)
- [X] Hantering av misslyckade inloggningsförsök (time-out/annan åtgärd efter X antal misslyckade inloggningsförsök)
- [X] Session hantering (inaktivitets/tidsbegr. autolog-out, return/inaktivera cookies after expired session)
- [X] JWT/Token säkerhet (kräv token för requesthantering)

### 3.2 Behörighetskontroll

Kryssa i det som finns med/det ni har hanterat eller ska hantera i er applikation

- [X] Olika användarnivåer/roller
- [X] Åtkomstkontroll för endpoints (t.ex., ej  hostverifierade användare kan ej POST listing, och ej admin kan inte DELETE annan user)
- [X] Validering av användarrättigheter (check token/role)

## 4. API Säkerhet

### 4.1 Input Validering

Kryssa i det som finns med/det ni har hanterar eller ska hantera i er applikation. Kryssa i även om vissa är disabled men skriv inom parentes disabled

- [ ] Validering av alla användarinput (nej, inte oinloggade användare som surfar runt???? vad räknas som validering?? vi är osäkra)
- [X] Skydd mot SQL Injection (validate user input via spring security features)
- [X] Skydd mot XSS (kanske är för svårt att implementera?)
- [X] Skydd mot CSRF (disabled during development

### 4.2 API Endpoints

Kryssa i det som finns med/det ni har hanterat eller ska hantera i er applikation

- [X] HTTPS användning (kommer inte användas i dev)
- [X] Rate limiting (time-out/limit för antal identiska request per tidsenhet?)
- [X] CORS konfiguration (ja, men först aktuellt för front end, postman ignorerar)
- [X] Error handling (inga känsliga felmeddelanden, kommentar: bara detaljerade felmeddelanden under utveckling, sen ska de generaliseras)

## 5. Implementerade Säkerhetsåtgärder

Plocka ut det delar ur Spring Security som ni tycker är viktigast, räcker med ett par kod snuttar.
Lista konkreta säkerhetsimplementeringar:
1. Hash och salt password (BCryptPassword)
2. Authenitication token med expiry (incl. JwtFilter for request validation)
4. Role based access restriction to end-points

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

     public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
         this.jwtAuthenticationFilter = jwtAuthenticationFilter;
     }

     //create Authentication manager for user/request authenitcation process
     @Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
         return authenticationConfiguration.getAuthenticationManager();
     }

     //main config for security filter and rules
     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                 // CSRF, disable in dev (nb! should only be disabled during development, not when going live)
                 .csrf(csrf -> csrf.disable())
                 // end-point access restriction by role
                 .authorizeHttpRequests(auth -> auth
                         //only admin can access things under url admin
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                         //only logged in users (any role) can access
                         .requestMatchers("/user/**").hasAnyRole()
                         //any user, e.g., access to login page
                         .requestMatchers("/auth/**").permitAll()
                         //all other urls, only logged in users
                         .anyRequest().authenticated()
                 )
                 // due session due to jwt statuslessness
                 .sessionManagement(session -> session
                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 )
                 // add jwt filter before the standard filer (should be before standard filter)
                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
         return http.build();
     }

     //defining hash and salt strength for password (common strength is 10-12)
     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder(12);
     }

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
