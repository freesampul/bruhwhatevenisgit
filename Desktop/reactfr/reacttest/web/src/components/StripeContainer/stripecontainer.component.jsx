import { loadStripe } from '@stripe/stripe-js';
import {Elements} from '@stripe/react-stripe-js';
import PaymentForm from '../paymentform/paymentform.component';
import React from 'react';

const PUBLIC_KEY = 'pk_test_51MKsf9DT4vO9oNMHcr5fVekm0L7XxNWmvFFePcgjmEkzdViRmaRSu93GDOYX767wVbwgLq0SShCizScEqaAGuScw00xq01VdPt'
const stripeTestPromise = loadStripe(PUBLIC_KEY);

export default function StripeContainer() {
  return (
    <Elements stripe={stripeTestPromise}>
        <PaymentForm />
    </Elements>
  )
}
