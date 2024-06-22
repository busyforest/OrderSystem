-- -- 插入示例菜品数据
INSERT INTO dish (dish_id, seller_id, name, price, picture, description, ingredients, nutrition_information, possible_allergens)
VALUES
    (1, 100000, '原味鸡', 50, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E5%8E%9F%E5%91%B3%E9%B8%A1.jpg', '酥脆多汁的鸡肉', '鸡肉, 香料, 面粉', '240卡, 20克蛋白质', '麸质'),
    (2, 100000, '香辣鸡腿堡', 40, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E9%A6%99%E8%BE%A3%E9%B8%A1%E7%BF%85.jpg', '香辣鸡肉汉堡', '鸡肉, 面包, 生菜, 蛋黄酱', '450卡, 25克蛋白质', '麸质, 蛋'),
    (3, 100000, '香辣鸡米花', 30, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E9%A6%99%E8%BE%A3%E9%B8%A1%E7%B1%B3%E8%8A%B1.jpg', '香辣鸡肉块', '鸡肉, 香料, 面粉', '300卡, 15克蛋白质', '麸质'),
    (4, 100000, '土豆泥', 20, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E5%9C%9F%E8%B1%86%E6%B3%A5.jpg', '奶油土豆泥配肉汁', '土豆, 肉汁', '150卡, 3克蛋白质', '乳制品'),
    (5, 100000, '凉拌卷心菜', 15, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E5%87%89%E6%8B%8C%E5%8D%B7%E5%BF%83%E8%8F%9C.jpg', '新鲜爽脆的凉拌卷心菜', '卷心菜, 胡萝卜, 蛋黄酱', '100卡, 1克蛋白质', '蛋');
INSERT INTO dish (dish_id, seller_id, name, price, picture, description, ingredients, nutrition_information, possible_allergens)
VALUES
    (6, 100000, '黄金鸡块', 35, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E9%BB%84%E9%87%91%E9%B8%A1%E5%9D%97.jpg', '金黄色酥脆的鸡块', '鸡肉, 香料, 面粉', '300卡, 18克蛋白质', '麸质'),
    (7, 100000, '香甜玉米', 10, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E9%A6%99%E7%94%9C%E7%8E%89%E7%B1%B3.jpg', '香甜的玉米杯', '玉米, 黄油', '150卡, 3克蛋白质', '乳制品'),
    (8, 100000, '劲脆鸡腿堡', 45, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E5%8A%B2%E8%84%86%E9%B8%A1%E8%85%BF%E5%A0%A1.jpg', '酥脆的鸡腿汉堡', '鸡肉, 面包, 生菜, 番茄酱', '480卡, 26克蛋白质', '麸质'),
    (9, 100000, '香辣鸡翅', 28, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E9%A6%99%E8%BE%A3%E9%B8%A1%E7%BF%85.jpg', '香辣入味的鸡翅', '鸡翅, 香料, 面粉', '320卡, 22克蛋白质', '麸质'),
    (10, 100000, '冰淇淋圣代', 20, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E5%9C%A3%E4%BB%A3.jpg', '奶油香草冰淇淋配巧克力酱', '牛奶, 糖, 巧克力酱', '200卡, 4克蛋白质', '乳制品');
-- 插入喜茶的示例菜品数据
INSERT INTO dish (dish_id, seller_id, name, price, picture, description, ingredients, nutrition_information, possible_allergens)
VALUES
    (11, 100001, '芝士芒芒', 35, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E8%8A%9D%E5%A3%AB%E8%8A%92%E8%8A%92.jpg', '香浓芝士配新鲜芒果', '芒果, 芝士, 牛奶', '250卡, 6克蛋白质', '乳制品'),
    (12, 100001, '桃桃波波茶', 30, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E6%A1%83%E6%A1%83%E6%B3%A2%E6%B3%A2%E8%8C%B6.jpg', '桃味果茶配波波', '桃, 茶, 波波', '200卡, 2克蛋白质', '无'),
    (13, 100001, '奶茶波波冰', 28, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E5%A5%B6%E8%8C%B6%E6%B3%A2%E6%B3%A2%E5%86%B0.jpg', '奶茶味波波冰', '奶茶, 波波', '220卡, 3克蛋白质', '乳制品'),
    (14, 100001, '满杯红柚', 32, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E6%BB%A1%E6%9D%AF%E7%BA%A2%E6%9F%9A.jpg', '新鲜红柚果茶', '红柚, 茶', '180卡, 2克蛋白质', '无'),
    (15, 100001, '满杯橙子', 30, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E6%BB%A1%E6%9D%AF%E6%9F%9A%E5%AD%90.jpg', '新鲜橙子果茶', '橙子, 茶', '190卡, 2克蛋白质', '无'),
    (16, 100001, '静冈抹茶', 35, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E9%9D%99%E5%86%88%E6%8A%B9%E8%8C%B6.jpg', '日本静冈抹茶', '抹茶, 牛奶', '240卡, 4克蛋白质', '乳制品'),
    (17, 100001, '白丸子豆乳茶', 32, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E7%99%BD%E4%B8%B8%E5%AD%90%E8%B1%86%E4%B9%B3%E8%8C%B6.jpg', '豆乳茶配白丸子', '豆奶, 白丸子', '210卡, 3克蛋白质', '大豆'),
    (18, 100001, '布蕾珍珠奶茶', 33, 'https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/%E5%B8%83%E9%9B%B7%E7%8F%8D%E7%8F%A0%E5%A5%B6%E8%8C%B6.jpg', '奶茶配布蕾和珍珠', '奶茶, 布蕾, 珍珠', '260卡, 5克蛋白质', '乳制品');
