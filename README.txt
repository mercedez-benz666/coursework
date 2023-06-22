Конфигурация логгера
 java -jar -Dlog4.configurationFile=log4j.properties Project.jar
 
 Значения для [provider]: DB, XML, CSV
 
  Создать пользователя
 java -jar -Dpath=config.properties Project.jar -userCreate [provider] [login] [password] [userType] 

Получить пользователя
java -jar -Dpath=config.properties Project.jar -userGet [provider] [id] 

 
Удалить пользователя 
java -jar -Dpath=config.properties Project.jar -userDelete [provider] [id] 


Сохранить услугу
Тип услуги принимает значения:
delivery
storage

insurance имеет тип boolean, принимает либо 0, либо 1

для delivery:
java -jar -Dpath=config.properties Project.jar -actionSave [provider] delivery [heightProduct] [widthProduct] [lenght] [insurance] [distance] [country] [numberAction][idUser]


для storage:
java -jar -Dpath=config.properties Project.jar -actionSave [provider] storage [heightProduct] [widthProduct] [lenght] [insurance] [dateCount] [country] [numberAction] [idUser]


 
Получить услугу, которую оформил пользователь
java -jar -Dpath=config.properties Project.jar -actionGet [provider] [userId]


Удалить услугу
Тип услуги принимает значения:
delivery
storage
java -jar -Dpath=config.properties Project.jar -actionDelete [provider] [actionType] [id]


Пользователь добавляет доставку к заказу
java -jar -Dpath=config.properties Project.jar -userAddDelivery [provider] [idUser][lenghtProduct] [heightProduct] [widthProduct] [distance]


Пользователь добавляет хранение к заказу
java -jar -Dpath=config.properties Project.jar -userAddStorage [provider] [idUser][lenghtProduct] [heightProduct] [widthProduct] [dayCount]

Пользователь добавляет страховку к доставке
java -jar -Dpath=config.properties Project.jar -userAddDeliveryInsurance [provider] [idUser]

Пользователь добавляет страховку к хранению
java -jar -Dpath=config.properties Project.jar -userAddStorageInsurance [provider] [idUser]

Пользователь оплачивает заказ. Заказ записывается в историю пользователя
java -jar -Dpath=config.properties Project.jar -userPayOrder [provider] [idUser] [paySum]

