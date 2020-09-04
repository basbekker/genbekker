var path = require('path');

module.exports = {
    entry: {
    	app: './src/main/resources/js/app.js',
    	dice: './src/main/resources/js/static.js/game/dice.js',
    	tictactoe: './src/main/resources/js/static.js/game/tictactoe.js'
    },
    devtool: 'sourcemaps',
    mode: 'development',
    cache: true,
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            }
        ]
    }
};
