// require
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const api = require('./routes/api');
const database = require('./config/connect');
const PhoneModel = require('./models/Phone')
//
const app = express();
//Tạo port
const PORT = 3000;
// body parser
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());
app.use('/api', api);
// connect db
database.connect();
//Chạy server
app.listen(PORT, () => {
    console.log(`Server is running the port ${PORT}`);
});
//
// app.set('view engine','ejs');
const multer = require('multer');
const storege = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'public/uploads')
    },
    filename: (req, file, cb) => {
        cb(null, file.fieldname + " " + Date.now() + file.originalname);
    },
});
const upload = multer({ storage: storege })

app.get('/list', async (req, res) => {
    let phones = await PhoneModel.find();
    console.log(phones);
    res.send(phones)
})

app.post('/list/add', async (req, res) => {
    try {
        const phone = req.body; 
        const newPhone = await PhoneModel.create(phone);
        console.log(newPhone);
        const phones = await PhoneModel.find();
        res.send(phones)
    } catch (error) {
        res.status(500).send(error);
    }
})
app.put('/list/:id', async (req, res) => {
    try {
        const phone = await PhoneModel.findById(req.params.id);
        await phone.updateOne({ '$set': req.body });
        res.status(200).json("Sửa thành công")
    } catch (error) {
        res.status(500).json(err)
    }
})
app.delete('/list/:id', async (req, res) => {
    try {
        const phoneId = req.params.id;
        console.log(phoneId);
        const deletePhone = await spModel.findByIdAndDelete(phoneId);
        await deletePhone.deleteOne({ '$set': req.body });
        res.status(200).json("Xóa thành công")
    } catch (error) {
        res.status(500).json(err)
    }
})
