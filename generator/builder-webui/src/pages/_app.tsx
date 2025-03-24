/* eslint-disable react/jsx-props-no-spreading */
import React from 'react'
import { AppProps } from 'next/app'

import '@/styles/tailwind.css'

const App = ({ Component, pageProps: { session, ...pageProps } }: AppProps) => <Component {...pageProps} />

export default App
