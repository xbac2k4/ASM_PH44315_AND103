const mongoose = require('mongoose');

// khai báo food
const PhoneSchema = mongoose.Schema({
    hinhanh: {
        type: String,
    },
    ten: {
        type: String,
        required: true,
    },
    hang: {
        type: String,
        required: true,
    },
    gia: {
        type: Number,
        required: true,
    },
    soluong: {
        type: Number,
        required: true,
    },
});

const Phone = mongoose.model('phone', PhoneSchema);

module.exports = Phone;