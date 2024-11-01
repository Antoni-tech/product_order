/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
      "./node_modules/flowbite/**/*.js",
      './client/*.html',
      './src/**/*.{js,jsx,ts,tsx}',
      './imports/ui/**/*.{js,jsx,ts,tsx}',
      'node_modules/flowbite-react/**/*.{js,jsx,ts,tsx}'
  ],
  theme: {
    extend: {},
  },
  plugins: [require('flowbite/plugin')],
}

