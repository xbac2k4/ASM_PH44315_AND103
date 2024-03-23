const express = require('express');
const router = express.Router();
const PhoneModel = require('../models/Phone');
const database = require('../config/connect');


router.get("/list", async (req, res) => {

    database.connect()

    let phones = await PhoneModel.find();

    console.log(phones);

    res.send(phones);
});
// Thêm sản phẩm
router.post("/add-phone", async (req, res) => {
    try {
        const { ten, hang, gia, soluong } = req.body;

        // Tạo một instance mới của model sản phẩm
        const newPhone = new server.PhoneModel({
            avatar,
            ten,
            hang,
            gia,
            soluong,
        });

        // Lưu sản phẩm mới vào cơ sở dữ liệu
        const savedPhone = await newPhone.save();

        res.status(201).json(savedPhone); // Trả về sản phẩm vừa được tạo thành công
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// Sửa sản phẩm
router.put("/update/:id", async (req, res) => {
    try {
        const { id } = req.params;
        const { avatar, ten, hang, gia, soluong } = req.body;

        const updatedPhone = await PhoneModel.findByIdAndUpdate(
            id,
            { avatar, ten, hang, gia, soluong },
            { new: true }
        );

        if (!updatedPhone) {
            return res.status(404).json({ message: "Không tìm thấy sản phẩm" });
        }

        res.json(updatedPhone);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// Xóa sản phẩm
router.delete("/delete/:id", async (req, res) => {
    try {
        const { id } = req.params;

        const deletedPhone = await PhoneModel.findByIdAndDelete(id);

        if (!deletedPhone) {
            return res.status(404).json({ message: "Không tìm thấy sản phẩm" });
        }

        res.json({ message: "Xóa sản phẩm thành công" });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

module.exports = router;