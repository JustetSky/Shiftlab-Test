## API Эндпоинты

### Sellers
```
POST /sellers               # Создать продавца
GET  /sellers               # Получить всех продавцов
GET  /sellers/{sellerId}    # Получить продавца по ID
PUT  /sellers/{sellerId}    # Обновить информацию о продавце по ID
DELETE sellers/{sellerId}   # Удалить продавца
```

### Transactions
```
POST /transactions                     # Создать транзакцию
GET  /transactions                     # Получить все транзакции
GET  /transactions/{transactionsId}    # Получить транзакцию по ID
GET  /transactions/seller/{sellerId}   # Получить транзакции продавца
```

### Analytics
```
GET /analytics/most-productive/{period}   # Самый продуктивный продавец
GET /analytics/sellers/total-less-than    # Продавцы с суммой транзакций меньше N
GET /analytics/best-period/{sellerId}     # Лучший период продавца
```

