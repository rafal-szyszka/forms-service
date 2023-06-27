# Forms - microservice
Podstawowy mikroserwis odpowiedzialny za aplikacyjny interfejs dostępu do danych tj. odczytu i zapisu. Serwis pozwala na
tworzenie definicji formularzy, dzięki którym końcowi użytkownicy mogą wprowadzać i odczytywać dane.

Serwis pozwala na komunikację poprzez RestAPI, które opisane jest poniżej.

## Opis działania
Koncepcja działania tego serwisu jest następująca. Jedynym dostępnym dla użytkownika i innych elementów systemu, interfejsem
wprowadzania danych będzie ten mikroserwis -> Forms. Jako jedyny serwis komunikuje się z serwisem danych i ma możliwość 
pobrania z niego danych za pomocą ProQLQuery [(DataService)](https://gitlab.sonelli-group.com:444/bpower3/micro-services/data-service), 
oraz zapisać do niego danych za pomocą ProQLCommand [(DataService)](https://gitlab.sonelli-group.com:444/bpower3/micro-services/data-service).

### Sprzężenie z modelem

Głównym założeniem tego mikroserwisu jest sprzężenie z metadanymi modelu, dzięki czemu użytkownik nie musi znać i rozumieć
struktury naszych danych. Sprzężenie oznacza, że formularz sam wie o zależnościach między polami na poziomie bazodanowym
i nie jest potrzebna żadna specjalistyczna wiedza aby taki formularz utworzyć. Kolejną zaletą tego rozwiązania ma być
możliwość utworzenia formularza za pomocą jedynie kilku kliknięć, np.: wyborze z pośród dostępnych pól tylko tych
które w danym momencie są porządane.

### Rozszerzanie funkcjonalności pola

#### Dekoracje

Kolejnym atutem tego rozwiązania będą dekoracje pola, czyli funkcje które rozszerzą podstawowe działanie każdego z pól o 
nowe opcje. Dzięki wydzieleniu tego od głównego modelu pola, zyskujemy na wydajności i przejrzystości rozwiązania. Dekoracje
mogą odpowiadać za określanie widoczności pól (dekorator HiddenFieldDecorator), zależnego ukrywania pól (dekorator DependantHiddenFieldDecorator),
wymagalności pola (RequiredFieldDecorator) itp.

<details>
<summary>Dostępne dekoratory:</summary>
- in progress
</details>

#### Źródła danych

Następnym rozszerzeniem funkcjonalności pól są źródła danych. Źródło danych w postaci ProQLQuery można dodać do wybranego pola aby zawęzić
ilość rekordów pobieranych ze zdefiniowanej relacji pola w modelu. Np.: pole wyboru klienta, można zawęzić niestandardowym warunkiem,
określajacym, że do wyboru powinni być dostepni tylko klienci z Polski, lub z określonej listy numerów NIP.

### Komunikacja z DataService
Komunikacja z serwisem danowym opiera się na trzech typach:
1. Pobieranie metadanych z serwisu danowego gdzie opisana tam została koncepcja wykorzystania cache'u, 
która pozwoli realizowac pobieranie tych danych sprawniej.[(DataService)](https://gitlab.sonelli-group.com:444/bpower3/micro-services/data-service)
2. Pobieranie danych za pomocą ProQLQuery
3. Zapis danych za pomocą ProQLCommand


### Przykłady działania

#### 1. Pobranie pustego formularza - opis działania

Wykonanie żądania pobrania formularza (zasób nr 3). W odpowiedzi przychodzą dane o wszystkich dostępnych polach. 
(Patrz przykład odpowiedzi) Uzyskane dane są kompletne dla zbudowania widoku formularza. W przypadku pól, które posiadają
wartości do wyboru (jak pole wyboru użytkownika / klienta) pole będzie zawierało wartość `dataUrl` z adresem url 
pod którym dane będą dostępne np.:
<details>
<summary>Przykład pola</summary>

```json
{
  "id": "6466296f99728127297409fc",
  "label": "Performer",
  "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=438234bsdfds92fn",
  "persistenceData": {
    "constraints": [
      "Required",
      "NotInsertable"
    ],
    "multiplicity": "SINGULAR"
  }
}
```
</details>
Zrealizowane to będzie w ten sposób aby zwiększyć prędkość uzyskania danych koniecznych do wyświetlenia formularza, same
wartości pola mogą być pobrane później. Ważne: na backendzie te dane będą pobierane podczas przygotowywania formularza tak
aby odpowiedź na żądanie klienta była prawie natychmiastowa.

W przypadku pól zależnych np.: pole `projekt` może mieć określone wartości jedynie wtedy kiedy zależne pole `klient` będzie
miało wybraną wartość. Dane pola `klient` będą zawierały dodatkową wartość `updateUrl` w którym znajdzie się adres zasobu, którego
odpowiedź będzie zawierała wartości dla wszystkich pól od pola `klient` zależnych.
<details>
<summary>Przykład pola</summary>

```json
{
  "id": "6466296f99728127297409fb",
  "label": "Client",
  "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=7368fsdfasdf532b",
  "updateUrl": "https://forms.service.bpower2.com/v1/data/?t=7234svdmg49281gf",
  "persistenceData": {
    "constraints": [
      "Required",
      "NotInsertable"
    ],
    "multiplicity": "SINGULAR"
  }
}
```
</details>
Zrealizowane to będzie w ten sposób aby zredukować do zera potrzebe programowania logiki obsługi formularza poza API.
Dzięki temu rozwiązaniu wszelkie integracje z naszym rozwiązaniem opierają się o jasne i przejrzyste zasoby, z obsługą 
całej logiki po naszej stronie.

#### 2. Pobranie formularza z danymi

Wykonanie żądania pobrania formularza z danymi (zasób nr 4). W odpowiedzi przychodzą dane o wszystkich dostępnych polach 
rozszerzone o pole wartości. (Patrz przykład odpowiedzi)

## RestAPI


<details>
<summary>
<code>GET</code> <code>/v1/meta</code> <code>Pobranie listy dostępnych modeli</code>
</summary>

#### Lista dostępnych modeli
Zasób pozwala pobrać listę modeli dla których jest możliwość utworzenia formularza na dane wejściowe.
Odpowiedzią tego zasobu jest list obiektów składających się z 3 atrybutów:  
 - `module` = moduł/grupa do której należy model, jest to tylko dla celów rozróżnienia modeli tej samej nazwy wykorzystywanych 
inaczej w zależności od problemu biznesowego
 - `name` = nazwa modelu
 - `primitive` = czy jest to typ prymitywny (`String`, `Long`, `DateTime`) czy model złożony, czyli model biznesowy

#### Parametry
Brak

#### Content-Type
```application/json```

#### Przykładowe wywołanie
```http request
GET http://{{host}}/v1/meta
Content-Type: application/json
```


<details>
<summary><b>Przykładowa odpowiedź</b></summary>

```json
[
  {
    "module": "organization",
    "name": "AppUser",
    "primitive": false
  },
  {
    "module": "organization",
    "name": "Customer",
    "primitive": false
  },
  {
    "module": "prodactivvity",
    "name": "Project",
    "primitive": false
  },
  {
    "module": "prodactivvity",
    "name": "Task",
    "primitive": false
  }
]
```
</details>
<details>
<summary>Objaśnienie struktury odpowiedzi</summary>

```json
  {
    "module": "prodactivvity",
    "name": "Task",
    "primitive": false
  }
```
`module` - nazwa modułu, grupy modeli, jednostki organizacyjnej. Pozwala i służy jedynie do organizacji modeli w grupy o zbliżonej
odpowiedzialności / funkcjonalności  
`name` - nazwa modelu  
`primitive` - pole określa czy model jest zdefiniowany przez nas czyli nie jest domyślnym typem wybranego języka programowania,
czy właśnie jest takim domyślnym typem dostarczonym przez wybraną technologię. Wartość `false` implikuje, że model jest niestandardowy
czyli utworzony przez nas, wartość `true` z kolei informuje, że dany typ jest obsługiwany przez wykorzystywaną technologię
</details>

</details>

---

<details>
<summary>
<code>GET</code> <code>/v1/meta/{module}/{model}</code> <code>Pobranie szczegółów modelu</code>
</summary>

#### Szczegóły modelu
Zasób pozwala pobrać szczegółowe metadane wybranego modelu. Są one niezbędne do utworzenia formularza bo zawierają 
informację o polach, ich typach, zależnościach i ograniczeniach.

#### Parametry
> | nazwa    | typ      | typ danych | opis                |
> |----------|----------|------------|---------------------|
> | `module` | wymagany | string     | Nazwa modułu modelu |
> | `model`  | wymagany | string     | Nazwa modelu        |

#### Content-Type
```application/json```

#### Przykładowe wywołanie
```http request
GET http://{{host}}/v1/meta/organization/AppUser
Content-Type: application/json
```
<details>
<summary><b>Przykładowa odpowiedź</b></summary>

```json
[
  {
    "name": "id",
    "type": { 
      "module": "primitives",
      "name": "Long",
      "primitive": true
    },
    "constraints": [
      "Id"
    ],
    "multiplicity": "SINGULAR"
  },
  {
    "name": "email",
    "type": {
      "module": "primitives",
      "name": "String",
      "primitive": true
    },
    "constraints": [],
    "multiplicity": "SINGULAR"
  },
  {
    "name": "password",
    "type": {
      "module": "primitives",
      "name": "String",
      "primitive": true
    },
    "constraints": [],
    "multiplicity": "SINGULAR"
  },
  {
    "name": "name",
    "type": {
      "module": "primitives",
      "name": "String",
      "primitive": true
    },
    "constraints": [],
    "multiplicity": "SINGULAR"
  },
  {
    "name": "lastName",
    "type": {
      "module": "primitives",
      "name": "String",
      "primitive": true
    },
    "constraints": [],
    "multiplicity": "SINGULAR"
  }
]
```
</details>
<details>
<summary>Objaśnienie struktury odpowiedzi</summary>

```json
{
  "name": "id",
  "type": {
    "module": "primitives",
    "name": "Long",
    "primitive": true
  },
  "constraints": [
    "Id"
  ],
  "multiplicity": "SINGULAR"
}
```
`name` - nazwa pola w bazie, może być użyta jako domyślna etykieta pola w formularzu,  
`type` - definicja typów prymitywnych i niestandardowych, np.: `Long`(liczby), `String`(napisy), `LocalDate`(data), 
`LocalDateTime`(data i czas), `Customer`(Klient [niestandardowy])  
`constraints` - dodatkowe ograniczenia nałożone przez samą bazę danych (np.: rozmiar, wymagalność) oraz 
niestandardowe ograniczenia biznesowe (np.: maska).  
`multiplicity` - wielokrotność pola, informuje o tym, czy model pozwala zapisać więcej niż jedną wartość opisanego typu. 
Wartość `SINGULAR` oznacza, że tylko jedna wartość danego typu może być zapisana w tym polu, wartość `PLURAL` pozwala na
zapis wielu wartości opisanego typu, np.: `Customer` może mieć pole `projects` z wielokrotnością `PLURAL`, co pozwoli na 
zapis wielu projektów do tego klienta, przez jedno pole. (w starych formsach był to grid).



</details>

</details>

---

<details>
<summary>
<code>GET</code> <code>/v1/queries/{id}</code> <code>Pobranie struktury formularza</code>
</summary>

#### Struktura formularza
Zasób pozwala pobrać dane struktury formularza, czyli jego nazwę oraz listę jego pól wraz z ich specyficznymi ustawieniami
jeżeli istnieją. Warto tutaj zauważyć, że dekoracje, które mogą być obsłużone na backendzie, nie będą zwracane w tym 
zasobie

#### Parametry
> | nazwa | typ      | typ danych | opis          |
> |-------|----------|------------|---------------|
> | `id`  | wymagany | string     | Id formularza |

#### Content-Type
```application/json```

#### Przykładowe wywołanie
```http request
GET http://{{host}}/v1/queries/test_6b706fdf3f82
Content-Type: application/json
```
<details>
<summary><b>Przykładowa odpowiedź</b></summary>

```json
{
  "id": "6466296f99728127297409ff",
  "name": "Order task",
  "fields": [
    {
      "id": "6466296f99728127297409f7",
      "label": "Task name",
      "persistenceData": {
        "constraints": [
          "Required"
        ],
        "multiplicity": "SINGULAR"
      }
    },
    {
      "id": "6466296f99728127297409f8",
      "label": "Task Description",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      }
    },
    {
      "id": "6466296f99728127297409f9",
      "label": "Ordered",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      }
    },
    {
      "id": "6466296f99728127297409fa",
      "label": "Deadline",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      }
    },
    {
      "id": "6466296f99728127297409fb",
      "label": "Orderer",
      "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=7368fsdfasdf532b",
      "updateUrl": "https://forms.service.bpower2.com/v1/data/?t=7234svdmg49281gf",
      "persistenceData": {
        "constraints": [
          "Required",
          "NotInsertable"
        ],
        "multiplicity": "SINGULAR"
      }
    },
    {
      "id": "6466296f99728127297409fc",
      "label": "Performer",
      "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=438234bsdfds92fn",
      "persistenceData": {
        "constraints": [
          "Required",
          "NotInsertable"
        ],
        "multiplicity": "SINGULAR"
      }
    },
    {
      "id": "6466296f99728127297409fd",
      "label": "Customer",
      "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=93645bfsitfdf537",
      "persistenceData": {
        "constraints": [
          "NotInsertable"
        ],
        "multiplicity": "SINGULAR"
      }
    },
    {
      "id": "6466296f99728127297409fe",
      "label": "Project",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      }
    }
  ]
}
```
</details>

<details>
<summary>Objaśnienie struktury odpowiedzi</summary>

```json
{
  "id": "6466296f99728127297409ff",
  "name": "Order task",
  "fields": [
    {
      "id": "6466296f99728127297409fb",
      "label": "Orderer",
      "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=7368fsdfasdf532b",
      "updateUrl": "https://forms.service.bpower2.com/v1/data/?t=7234svdmg49281gf",
      "persistenceData": {
        "constraints": [
          "Required",
          "NotInsertable"
        ],
        "multiplicity": "SINGULAR"
      }
    }
  ]
}
```

`id` - id formularza,  
`name` - nazwa formularza,  
`fields` - lista pól,  
&emsp;`id` - id pola,  
&emsp;`label` - etykieta pola,  
&emsp;`dataUrl` - url do pobrania danych w przypadku pola z możlwością wyboru (np.: Select),  
&emsp;`updateUrl` - url do aktualizacji formularza w przypadku kiedy wartość pola implikuje zmiany wartości innych pól,  
&emsp;`persistenceData` - informacje potrzebne do zapisu, między innymi wielokrotność oraz ograniczenia pola  


</details>

</details>

---

<details>
<summary>
<code>GET</code> <code>/v1/queries/{id}/{dataId}</code> <code>Pobranie formularza z danymi</code>
</summary>

#### Formularz z danymi
Zasób pozwala pobrać formularz ze zmapowanymi danymi modelu na którym operuje.

#### Parametry
> | nazwa    | typ      | typ danych | opis                  |
> |----------|----------|------------|-----------------------|
> | `id`     | wymagany | string     | Id formularza         |
> | `dataId` | wymagany | Long       | Id danych do pobrania |

#### Content-Type
```application/json```

#### Przykładowe wywołanie
```http request
GET http://{{host}}/v1/queries/test_6b706fdf3f82/63521
Content-Type: application/json
```
<details>
<summary><b>Przykładowa odpowiedź</b></summary>

```json
{
  "id": "6466296f99728127297409ff",
  "name": "Order task",
  "fields": [
    {
      "id": "6466296f99728127297409f7",
      "label": "Task name",
      "persistenceData": {
        "constraints": [
          "Required"
        ],
        "multiplicity": "SINGULAR"
      },
      "value": "Example task"
    },
    {
      "id": "6466296f99728127297409f8",
      "label": "Task Description",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      },
      "value": "Example longer value"
    },
    {
      "id": "6466296f99728127297409f9",
      "label": "Ordered",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      },
      "value": "18.05.2023"
    },
    {
      "id": "6466296f99728127297409fa",
      "label": "Deadline",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      },
      "value": "22.05.2023"
    },
    {
      "id": "6466296f99728127297409fb",
      "label": "Orderer",
      "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=7368fsdfasdf532b",
      "updateUrl": "https://forms.service.bpower2.com/v1/data/?t=7234svdmg49281gf",
      "persistenceData": {
        "constraints": [
          "Required",
          "NotInsertable"
        ],
        "multiplicity": "SINGULAR"
      },
      "value": "Jan Kowalski"
    },
    {
      "id": "6466296f99728127297409fc",
      "label": "Performer",
      "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=438234bsdfds92fn",
      "persistenceData": {
        "constraints": [
          "Required",
          "NotInsertable"
        ],
        "multiplicity": "SINGULAR"
      },
      "value": "Sławomir Nowak"
    },
    {
      "id": "6466296f99728127297409fd",
      "label": "Customer",
      "dataUrl": "https://forms.service.bpower2.com/v1/data/?t=93645bfsitfdf537",
      "persistenceData": {
        "constraints": [
          "NotInsertable"
        ],
        "multiplicity": "SINGULAR"
      },
      "value": "BP2ML Sp. z o.o"
    },
    {
      "id": "6466296f99728127297409fe",
      "label": "Project",
      "persistenceData": {
        "constraints": [],
        "multiplicity": "SINGULAR"
      },
      "value": "Tasklytics"
    }
  ]
}
```
</details>

</details>

### Lista zadań
