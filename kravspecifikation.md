# Kravdokument
## Innehållsförteckning
1. Introduktion
1.1. Bakgrund
1.2. Syfte
1.3. Omfattning
2. Intressenter
3. Kravspecifikation
3.1. Funktionella krav
3.1.1. Bokningsdatum
3.1.2. Bokningspris
3.1.3. Bokningsstatus
3.2. Icke-Funktionella Krav
3.2.1. Underhåll och förbättring
3.2.2. Säkerhet
4. Prioriteringar och Beroenden

## 1. Introduktion
### 1.1. Bakgrund
AirBnB Clone applikationen är utvecklad för att privatpersoner ska kunna hyra ut boenden till varandra på ett säkert och smidigt sätt. Applikationsförbättringen som detta dokument berör syftar till att göra bokningsprocessen säkrare och minska risken för fel när det gäller t.ex. priskalkylering och uppdatering bokningsbara datum för ett boende.
### 1.2. Syfte
Syftet med detta dokument är att sammanställa och specificera krav på den förbättrade versionen av applikationen för att säkerställa att de specificerade kraven uppfylls under applikationsutvecklingsprocessen.
### 1.3. Omfattning
Detta dokument omfattar funktionella och icke-funktionella applikationskrav. Kraven inkluderar funktionalitet som applikationen redan uppfyller, men som ingår i delen i applikationen som ska vidareutvecklas.
## 2. Intressenter
#### Intressent – Roll
- Grupp A – Utvecklingsteam
- Grupp B – Slutanvändare
- Lärare – Produktägare
## 3. Kravspecifikation
### 3.1. Funktionella krav
#### 3.1.1. Bokningsdatum
- FK-001: Användare ska kunna boka boenden endast under tillgängliga datum för boendet
- FK-002: När en bokning görs så ska applikationen ta bort de bokade datumen från de tillgängliga datumen för boendet
- FK-003: När en bokning nekas eller cancelleras ska applikationen lägga tillbaka datumen för bokning tills boendets tillgängliga datum
#### 3.1.2. Bokningspris
- FK-004: Applikationen ska beräkna ett korrekt totalpris för en bokning utifrån antal dagar bokningen sträcker sig över och boendets pris per natt
#### 3.1.3. Bokningsstatus
- FK-005: Applikationen ska ge nya bokningar bokningsstatusen ”PENDING”
- FK-006: Använder som har lagt ut ett boende ska kunna acceptera och neka bokningar för sitt boende
- FK-007: Applikationen ska uppdatera bokningsstatus på en bokning om den accepteras eller nekas av användaren som äger boendet
### 3.2. Icke-Funktionella Krav
#### 3.2.1. Underhåll och förbättring
- IFK-001 Applikationens bokningssystem ska vara lätt att förstå och underhålla
- IFK-002: Hantering och kalkylering av bokningspris i applikationen ska vara strukturerat på ett sätt som enkelt går att vidareutveckla i framtiden (t.ex. med mer dynamisk prissättning eller rabatt)
- IFK-003: Hantering av bokningsstatus i applikationen ska vara strukturerat på ett sätt som enkelt går att vidareutveckla i framtiden (t.ex. med addition av fler bokningsstatusalternativ)
- IFK-004: Hantering av bokningsbara datum för ett boende ska vara felsäkrat på flera nivåer i applikationen
#### 3.2.2 Säkerhet
- IFK-005: Alla användarlösenord ska lagras krypterade
- IFK-006: Inga betalningsuppgifter ska lagras i applikationen

## 4. Prioriteringar och Beroenden
#### Krav-ID – Prioritet – Beroende
- FK-001 – Must Have – FK-002, FK-003
- FK-002 – Must Have
- FK-003 – Must Have
- FK-004 – Must Have
- FK-005 – Must Have
- FK-006 – Must Have – FK-007
- FK-007 – Must Have – FK-006
- IFK-001 – Must Have – IFK-002, IFK-003
- IFK-002 – Must Have
- IFK-003 – Must Have
- IFK-004 – Should Have
- IFK-005 – Must Have
- IFK-006 – Must Have

