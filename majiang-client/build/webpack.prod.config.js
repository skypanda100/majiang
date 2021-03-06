const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const cleanWebpackPlugin = require('clean-webpack-plugin');
const merge = require('webpack-merge');
const webpackBaseConfig = require('./webpack.base.config.js');
const fs = require('fs');
const path = require('path');
const package = require('../package.json');
const uglifyjs = require('uglifyjs-webpack-plugin');

fs.open('./compile.js', 'w', function (err, fd) {
    const buf = 'export default "production";';
    // fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {
    // });
    fs.write(fd, buf, 0, 'utf-8', function (err, written, buffer) {
    });
});

module.exports = merge(webpackBaseConfig, {
    output: {
        publicPath: '/dist/',
        filename: '[name].[hash].js',
        chunkFilename: '[name].[hash].chunk.js'
    },
    plugins: [
        new cleanWebpackPlugin(['dist/*'], {
            root: path.resolve(__dirname, '../')
        }),
        new ExtractTextPlugin({
            filename: '[name].[hash].css',
            allChunks: true
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: ['vender-exten', 'vender-base'],
            minChunks: Infinity
        }),
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"production"'
            }
        }),
        new uglifyjs(),
        new HtmlWebpackPlugin({
            title: 'majiang v' + package.version,
            favicon: './majiang.png',
            filename: '../index.html',
            template: './src/template/index.ejs',
            inject: false
        })
    ]
});
