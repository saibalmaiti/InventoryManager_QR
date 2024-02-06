# Inventory Manager

<p>This is an android app built with Java and Firebase for a small scale business, which needs to keep track of it's inventory. It uses QR based data entry system to indentify a product.</p>

---

## Demo

Here is a demo video you can check out

<div align="center">
  <video src="https://github.com/saibalmaiti/InventoryManager_QR/assets/51844015/d894e26f-fbb2-4899-b25f-34f7609ad0e7"/>
</div>

## Made with
<img src="https://img.shields.io/badge/firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black"/> <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/> <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"/>

## Usage Instruction
You can download the app and try it from <a href="https://drive.google.com/file/d/1GoJ3-T2oyhA1bPcSopIs8pEYshA2f94f/view?usp=sharing">Download Link </a>

## Features
- User authentication using Firebase username, password-based authentication, and role-based authorization to worker dashboard and admin dashboard.
- From the admin dashboard, the admin can add a new product to the catalog and it will generate a new QR code which will be placed on the product, during manufacturing.
- Admin can keep track of the inventory from the dashboard, showing the products sold and available or in-stock products in a table.
- Admin can also delete and modify the product details, all the images and details are stored in Firebase storage and Firebase real-time database.
- Employees need to scan the product QR while a product is added to the stock and while selling they can scan and remove the product from the stock.

## Limitation
Currently, it does not incorporate a multibusiness model, which means the database can not support data from multiple businesses, so it can be used only for one shop/business at this moment.


