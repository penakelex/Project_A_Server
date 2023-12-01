# Meventer
# Содержание
- Аргументы запросов:
  - Пользователь
  - Мероприятие
- Результаты выполнения запросов
  - Общие
  - Пользователь
  - Мероприятия

## Аргументы запросов
### Пользователь
+ UserRegister
```kotlin
UserRegister(
    val phone: String?,
    val email: String?,
    val password: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val status: String
)
```
+ UserLogin
```kotlin
UserLogin(
    val phone: String?,
    val email: String?,
    val password: String
)
```
+ UserUpdate
```kotlin
UserUpdate(
    val phone: String?,
    val name: String?,
    val surname: String?,
    val patronymic: String?,
    val status: String?
)
```
### Мероприятия
+ EventCreate
```kotlin
EventCreate(
    val organizers: Array<UInt>,
    val name: String,
    val start: ULong,
    val end: ULong,
    val description: String,
    val picture: String,
    val participants: Array<UInt>?
)
```
+ EventEdit
```kotlin
EventEdit(
    val id: UInt,
    val organizers: Array<UInt>?,
    val name: String?,
    val start: ULong?,
    val end: ULong?,
    val description: String?,
    val picture: String?,
)
```

## Результаты выполнения запросов
### Общие
+ Response
```kotlin
Response(
    val result: ResultResponse,
    val data: Type? = null
)
```
+ ResultResponse
```kotlin
ResultResponse(
    val statusCode: Short,
    val message: String
)
```
### Пользователь
+ User
```kotlin
User(
    val phone: String?,
    val email: String?,
    val name: String,
    val surname: String,
    val patronymic: String,
    val status: String
)
```
### Мероприятия
+ Events
```kotlin
Events(
    val organizers: Array<UInt>,
    val name: String,
    val start: ULong,
    val end: ULong,
    val description: String,
    val picture: String
)
```