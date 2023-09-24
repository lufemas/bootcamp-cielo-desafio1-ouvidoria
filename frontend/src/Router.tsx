import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import ListMessagesPage from './pages/ListMessagesPage';
import HomePage from './pages/HomePage';
import SendMessage from './pages/SendMessage';

const Router: React.FC = () => {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="*" element={<HomePage />} />
          <Route path="/" element={<HomePage />} />
          <Route path="/send-message" element={<SendMessage />} />
          <Route path="/customer" element={<SendMessage />} />
          <Route path="/list-messages" element={<ListMessagesPage />} />
          <Route path="/administrator" element={<ListMessagesPage />} />
          <Route path="/consume-messages" element={<ListMessagesPage />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
};

export default Router;
