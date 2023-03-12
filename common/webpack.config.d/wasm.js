const CopyWebpackPlugin = require('copy-webpack-plugin');
config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            '../../node_modules/sql.js/dist/sql-wasm.wasm',
            '../../node_modules/sql.js/dist/worker.sql-wasm.js'
        ]
    })
);
