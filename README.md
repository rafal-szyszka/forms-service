# Forms - microservice
Podstawowy mikroserwis odpowiedzialny za aplikacyjny interfejs dostępu do danych tj. odczytu i zapisu. Serwis pozwala na
tworzenie definicji formularzy, dzięki którym końcowi użytkownicy mogą wprowadzać i odczytywać dane.

Serwis pozwala na komunikację poprzez RestAPI, które opisane jest poniżej.

## RestAPI


<details>
<summary>
<code>GET</code> <code>/v1/formModels</code> <code>Pobranie listy dostępnych modeli</code>
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
GET http://{{host}}/v1/formModels
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
</details>

<details>
<summary>
<code>GET</code> <code>/v1/formModels/{module}/{model}</code> <code>Pobranie szczegółów modelu</code>
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
GET http://{{host}}/v1/formModels/organization/AppUser
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

</details>